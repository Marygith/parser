package com.pet.parser.tcp;


import com.pet.parser.events.CustomEvent;
import com.pet.parser.events.EventsPublisher;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.integration.config.EnableIntegration;

import java.util.Timer;
import java.util.TimerTask;

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

    public ClientConf(EventsPublisher eventsPublisher) {
        this.bootstrap = new Bootstrap();
        this.handler = new TcpHandler(eventsPublisher);
        this.timer = new Timer();
    }

    @Bean
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

    private void scheduleConnect(long millis) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                connect();
            }
        }, millis);
    }

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

    @EventListener(condition = "#event.eventType eq 'TcpEvent'")
    public void sendResponse(CustomEvent event) {
        handler.channelWrite(event.getPayload());
    }
}


