package com.prayerlaputa.im.study.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.prayerlaputa.im.study.serialize.Serializer;
import com.prayerlaputa.im.study.serialize.SerializerAlogrithm;

/**
 * @author chenglong.yu
 * created on 2020/9/17
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlogrithm() {
        return SerializerAlogrithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
