package com.prayerlaputa.im.study.protocol.response;

import com.prayerlaputa.im.study.protocol.Packet;
import com.prayerlaputa.im.study.protocol.command.Command;
import com.prayerlaputa.im.study.server.session.Session;
import lombok.Data;

import java.util.List;

/**
 * @author chenglong.yu
 * created on 2020/9/22
 */
@Data
public class ListGroupMembersResponsePacket extends Packet {

    private boolean success;
    private String groupId;
    private String reason;
    private List<Session> sessionList;

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_MEMBERS_RESPONSE;
    }
}
