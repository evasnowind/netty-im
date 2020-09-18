package com.prayerlaputa.im.study.attribute;

import com.prayerlaputa.im.study.constval.IMConst;
import io.netty.util.AttributeKey;

/**
 * @author chenglong.yu
 * created on 2020/9/18
 */
public interface Attributes {
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance(IMConst.CLIENT_LOGIN);
}
