package com.prayerlaputa.im.study.protocol.command;

/**
 * @author chenglong.yu
 * created on 2020/9/17
 */
public interface Command {
    /*
    登录请求与响应
     */
    Byte LOGIN_REQUEST = 1;
    Byte LOGIN_RESPONSE = 2;
    /*
    发送消息与消息反馈
     */
    Byte MESSAGE_REQUEST = 3;
    Byte MESSAGE_RESPONSE = 4;


}
