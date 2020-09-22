package com.prayerlaputa.im.study.protocol.request;

import com.prayerlaputa.im.study.protocol.Packet;
import com.prayerlaputa.im.study.protocol.command.Command;

/**
 * @author chenglong.yu
 * created on 2020/9/22
 */
public class LogoutRequestPacket extends Packet {


    @Override
    public Byte getCommand() {
        return Command.LOGOUT_REQUEST;
    }
}
