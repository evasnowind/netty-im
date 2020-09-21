package com.prayerlaputa.im.study.protocol.request;

import com.prayerlaputa.im.study.protocol.Packet;
import com.prayerlaputa.im.study.protocol.command.Command;
import lombok.Data;

/**
 * @author chenglong.yu
 * created on 2020/9/17
 */
@Data
public class LoginRequestPacket extends Packet {

    private String userId;
    private String userName;
    private String password;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
