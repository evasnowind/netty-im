package com.prayerlaputa.im.study.server.handler;

import com.prayerlaputa.im.study.protocol.request.JoinGroupRequestPacket;
import com.prayerlaputa.im.study.protocol.response.JoinGroupResponsePacket;
import com.prayerlaputa.im.study.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * @author chenglong.yu
 * created on 2020/9/22
 */
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket joinGroupRequestPacket) throws Exception {
        // 1. 获取群对应的 channelGroup
        String groupId = joinGroupRequestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        // 构造加群响应发送给客户端
        JoinGroupResponsePacket joinGroupResponsePacket = new JoinGroupResponsePacket();

        // 2. 群组存在，则新加入的channel加入该channelGroup
        if (null != channelGroup) {
            //加入群组
            channelGroup.add(ctx.channel());
            //返回加入成功消息
            joinGroupResponsePacket.setGroupId(groupId);
            joinGroupResponsePacket.setSuccess(true);
        } else {
            //返回加入群组失败
            joinGroupResponsePacket.setSuccess(false);
            joinGroupResponsePacket.setReason("没有找到对应群组！");
        }
        // 3. 发送消息
        ctx.channel().writeAndFlush(joinGroupResponsePacket);
    }
}
