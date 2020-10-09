package com.prayerlaputa.im.study.protocol.response;

import com.prayerlaputa.im.study.protocol.Packet;
import com.prayerlaputa.im.study.protocol.command.Command;

public class HeartBeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }
}
