package com.prayerlaputa.im.study.client.handler;

import com.prayerlaputa.im.study.protocol.request.LogoutRequestPacket;
import com.prayerlaputa.im.study.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author chenglong.yu
 * created on 2020/9/22
 */
public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket logoutRequestPacket) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }
}
