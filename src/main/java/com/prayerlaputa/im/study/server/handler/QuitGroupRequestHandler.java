package com.prayerlaputa.im.study.server.handler;

import com.prayerlaputa.im.study.protocol.request.QuitGroupRequestPacket;
import com.prayerlaputa.im.study.protocol.response.QuitGroupResponsePacket;
import com.prayerlaputa.im.study.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * @author chenglong.yu
 * created on 2020/9/22
 */
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestPacket requestPacket) throws Exception {
        // 1. 获取群对应的 channelGroup，然后将当前用户的 channel 移除
        String groupId = requestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        channelGroup.remove(ctx.channel());

        // 2. 构造退群响应发送给客户端
        QuitGroupResponsePacket responsePacket = new QuitGroupResponsePacket();
        responsePacket.setGroupId(groupId);
        responsePacket.setSuccess(true);
        ctx.channel().writeAndFlush(responsePacket);

        //如果群组内成为0，则将该群组删除
        if (channelGroup.size() == 0) {
            SessionUtil.unBindChannelGroup(groupId);
            System.out.println("groupId=" + groupId + "成员人数为0，已被清理！");
        }
    }
}
