package com.prayerlaputa.im.study.util;

import com.prayerlaputa.im.study.attribute.Attributes;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * @author chenglong.yu
 * created on 2020/9/18
 */
public class LoginUtil {

    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(Boolean.TRUE);
    }

    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAtr = channel.attr(Attributes.LOGIN);
        return loginAtr.get() != null ? loginAtr.get() : false;
    }
}
