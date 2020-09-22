package com.prayerlaputa.im.study.util;

import java.util.UUID;

/**
 * @author chenglong.yu
 * created on 2020/9/22
 */
public class IDUtil {

    public static String randomId() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}
