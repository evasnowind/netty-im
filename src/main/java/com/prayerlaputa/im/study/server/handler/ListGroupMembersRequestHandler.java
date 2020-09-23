package com.prayerlaputa.im.study.server.handler;

import com.prayerlaputa.im.study.protocol.request.ListGroupMembersRequestPacket;
import com.prayerlaputa.im.study.protocol.response.ListGroupMembersResponsePacket;
import com.prayerlaputa.im.study.server.session.Session;
import com.prayerlaputa.im.study.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenglong.yu
 * created on 2020/9/22
 */
@ChannelHandler.Sharable
public class ListGroupMembersRequestHandler extends SimpleChannelInboundHandler<ListGroupMembersRequestPacket> {

    public static final ListGroupMembersRequestHandler INSTANCE = new ListGroupMembersRequestHandler();

    private ListGroupMembersRequestHandler() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersRequestPacket requestPacket) {
        // 1. 获取群的 ChannelGroup
        String groupId = requestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        //简单写点判空处理
        if (null == channelGroup) {
            ListGroupMembersResponsePacket responsePacket = new ListGroupMembersResponsePacket();
            responsePacket.setSuccess(false);
            responsePacket.setReason("根据groupId=" + groupId + "查找群组失败！");
            ctx.writeAndFlush(responsePacket);
        } else {
            // 2. 遍历群成员的 channel，对应的 session，构造群成员的信息
            List<Session> sessionList = new ArrayList<>();
            for (Channel channel : channelGroup) {
                Session session = SessionUtil.getSession(channel);
                sessionList.add(session);
            }

            // 3. 构建获取成员列表响应写回到客户端
            ListGroupMembersResponsePacket responsePacket = new ListGroupMembersResponsePacket();
            responsePacket.setGroupId(groupId);
            responsePacket.setSessionList(sessionList);
            ctx.writeAndFlush(responsePacket);
        }

    }
}
