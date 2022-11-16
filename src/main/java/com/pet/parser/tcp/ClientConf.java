package com.pet.parser.tcp;


import com.pet.parser.events.CustomEvent;
import com.pet.parser.events.EventsPublisherImpl;
import com.pet.parser.services.implementations.ParserServiceImpl;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.integration.config.EnableIntegration;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>Class that serves as a configuration of <b>tcp client</b>.</p>
 * Server's <i>ip</i> and <i>port</i> can be changed in config.properties file.
 */
@Configuration
@EnableIntegration
@PropertySource("classpath:config.properties")
public class ClientConf {

    @Value("${port}")
    private int port;

    @Value("${ip}")
    private String ip;

    private final Bootstrap bootstrap;
    private Channel channel;
    private final TcpHandler handler;
    private final Timer timer;

    /**
     * Class constructor.
     *
     * @param eventsPublisher see {@link EventsPublisherImpl}.
     */
    public ClientConf(EventsPublisherImpl eventsPublisher) {
        this.bootstrap = new Bootstrap();
        this.handler = new TcpHandler(eventsPublisher);
        this.timer = new Timer();
    }

    /**
     * A method that initiates a {@link Bootstrap} of tcp client
     * and schedules first attempt to connect server.
     *
     * @see TcpHandler
     */
    @PostConstruct
    void init() {
        this.bootstrap.group(new NioEventLoopGroup());
        this.bootstrap.channel(NioSocketChannel.class);
        this.bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(handler);
            }
        });
        scheduleConnect(10);
    }

    /**
     * A method that schedules new attempt to create connection with server in given amount of time.
     *
     * @param millis amount of time, measured in milliseconds.
     */
    private void scheduleConnect(long millis) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                connect();
            }
        }, millis);
    }


    /**
     * A method that attempts to connect server every second if server is inactive
     * or the connection was lost.
     *
     * @see ChannelFutureListener
     */
    private void connect() {
        ChannelFuture f = bootstrap.connect(ip, port);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (!future.isSuccess()) {
                    future.channel().close();
                    bootstrap.connect(ip, port).addListener(this);
                } else {
                    channel = future.channel();
                    channel.closeFuture().addListener((ChannelFutureListener) cfl -> scheduleConnect(1000));
                }
            }
        });
    }

    /**
     * <p>A method listening for {@link CustomEvent} event, which contains byte [] reply for server.</p>
     * <p>This reply is formed by {@link ParserServiceImpl}.</p>
     * Method passes reply to {@link TcpHandler}, that sends response to the server.
     *
     * @param event CustomEvent that is published by {@link EventsPublisherImpl}.
     */
    @EventListener(condition = "#event.eventType eq 'TcpEvent'")
    public void sendResponse(CustomEvent event) {
        handler.channelWrite(event.getPayload());
    }
}


