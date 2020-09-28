package com.prayerlaputa.im.study.serialize;

import com.prayerlaputa.im.study.serialize.impl.JSONSerializer;

/**
 * @author chenglong.yu
 * created on 2020/9/17
 */
public interface Serializer {
    Serializer DEFAULT = new JSONSerializer();


    /**
     * 序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
