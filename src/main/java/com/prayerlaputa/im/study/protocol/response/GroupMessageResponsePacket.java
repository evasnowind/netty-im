package com.prayerlaputa.im.study.protocol.response;

import com.prayerlaputa.im.study.protocol.Packet;
import com.prayerlaputa.im.study.protocol.command.Command;
import com.prayerlaputa.im.study.server.session.Session;
import lombok.Data;

/**
 * @author chenglong.yu
 * created on 2020/9/23
 */
@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;
    private Session fromUser;
    private String message;

    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_RESPONSE;
    }
}
