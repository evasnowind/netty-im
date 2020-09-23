package com.prayerlaputa.im.study.protocol.response;

import com.prayerlaputa.im.study.protocol.Packet;
import com.prayerlaputa.im.study.protocol.command.Command;
import lombok.Data;

/**
 * @author chenglong.yu
 * created on 2020/9/22
 */
@Data
public class QuitGroupResponsePacket extends Packet {

    private boolean success;
    private String reason;
    private String groupId;

    @Override
    public Byte getCommand() {
        return Command.QUIT_GROUP_RESPONSE;
    }
}
