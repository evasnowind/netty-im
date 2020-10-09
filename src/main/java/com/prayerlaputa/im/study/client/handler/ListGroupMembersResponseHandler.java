package com.prayerlaputa.im.study.client.handler;

import com.prayerlaputa.im.study.protocol.response.ListGroupMembersResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author chenglong.yu
 * created on 2020/9/22
 */
public class ListGroupMembersResponseHandler extends SimpleChannelInboundHandler<ListGroupMembersResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersResponsePacket responsePacket) throws Exception {
        if (!responsePacket.isSuccess()) {
            System.out.println("群[" + responsePacket.getGroupId() + "]中的人包括：" + responsePacket.getSessionList());
        } else {
            System.err.println("ListGroupMembersResponseHandler error:" + responsePacket.getReason());
        }
    }
}
