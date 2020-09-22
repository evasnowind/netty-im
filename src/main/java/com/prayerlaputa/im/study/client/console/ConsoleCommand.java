package com.prayerlaputa.im.study.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author chenglong.yu
 * created on 2020/9/22
 */
public interface ConsoleCommand {
    void exec(Scanner scanner, Channel channel);
}
