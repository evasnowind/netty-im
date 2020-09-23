package com.prayerlaputa.im.study.protocol.request;

import com.prayerlaputa.im.study.protocol.Packet;
import com.prayerlaputa.im.study.protocol.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chenglong.yu
 * created on 2020/9/23
 */
@Data
@AllArgsConstructor
public class GroupMessageRequestPacket extends Packet {

    private String toGroupId;
    private String message;

    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_REQUEST;
    }
}
