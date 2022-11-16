package com.pet.parser.tcp;

import com.pet.parser.events.CustomEvent;
import com.pet.parser.events.EventsPublisherImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * {@link ChannelInboundHandlerAdapter } class.
 * <p>Receives byte [] data from the server and sends replies to it
 * using {@link ChannelHandlerContext }.</p>
 */
@ChannelHandler.Sharable
public class TcpHandler extends ChannelInboundHandlerAdapter {


    private final EventsPublisherImpl eventsPublisher;
    private ChannelHandlerContext ctx;

    public TcpHandler(EventsPublisherImpl eventsPublisher) {
        this.eventsPublisher = eventsPublisher;
    }

    /**
     * A method that reads data from server and
     * publishes  {@link CustomEvent } with this data.
     *
     * @param ctx see  {@link ChannelHandlerContext }
     * @param msg received data
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        this.ctx = ctx;
        ByteBuf buf = (ByteBuf) msg;
        int n = buf.readableBytes();
        if (n > 0) {
            byte[] b = new byte[n];
            buf.readBytes(b);
            eventsPublisher.publishEvent(new CustomEvent(b, "GetDataEvent"));
        }
    }


    /**
     * A method that sends <i>data</i> to the server via  {@link ChannelHandlerContext }.
     *
     * @param data byte [] data
     */
    public void channelWrite(byte[] data) {
        ByteBuf answer = Unpooled.buffer(data.length + 1);
        byte[] del = new byte[]{10};
        answer.writeBytes(data);
        answer.writeBytes(del);
        ctx.writeAndFlush(answer);

    }
}

