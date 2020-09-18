package com.prayerlaputa.im.study.protocol.response;

import com.prayerlaputa.im.study.protocol.Packet;
import com.prayerlaputa.im.study.protocol.command.Command;
import lombok.Data;

/**
 * @author chenglong.yu
 * created on 2020/9/18
 */
@Data
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
