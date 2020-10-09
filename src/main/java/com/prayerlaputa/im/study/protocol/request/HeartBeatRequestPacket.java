package com.prayerlaputa.im.study.protocol.request;

import com.prayerlaputa.im.study.protocol.Packet;
import com.prayerlaputa.im.study.protocol.command.Command;

public class HeartBeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_REQUEST;
    }
}
