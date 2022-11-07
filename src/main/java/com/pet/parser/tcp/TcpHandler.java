package com.pet.parser.tcp;

import com.pet.parser.events.CustomEvent;
import com.pet.parser.events.EventsPublisher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class TcpHandler extends ChannelInboundHandlerAdapter {


    private final EventsPublisher eventsPublisher;
    private ChannelHandlerContext ctx;

    public TcpHandler(EventsPublisher eventsPublisher) {
        this.eventsPublisher = eventsPublisher;
    }

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


    public void channelWrite(byte[] data) {
        ByteBuf answer = Unpooled.buffer(data.length + 1);
        byte[] del = new byte[]{10};
        answer.writeBytes(data);
        answer.writeBytes(del);
        ctx.writeAndFlush(answer);

    }
}

