package com.prayerlaputa.im.study.server.handler;

import com.prayerlaputa.im.study.protocol.request.HeartBeatRequestPacket;
import com.prayerlaputa.im.study.protocol.response.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {
    public static final HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();

    private HeartBeatRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket requestPacket) {
//        System.out.println("get heart beat from " + ctx.channel());
        ctx.writeAndFlush(new HeartBeatResponsePacket());
    }
}
