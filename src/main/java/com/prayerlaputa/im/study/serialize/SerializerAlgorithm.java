package com.prayerlaputa.im.study.serialize;

/**
 * @author chenglong.yu
 * created on 2020/9/17
 */
public interface SerializerAlgorithm {
    /**
     * json 序列化
     */
    byte JSON = 1;

    /**
     * protostuff 序列化标识
     */
    byte PROTO_STUFF = 2;

    /**
     * kryo 序列化标识
     */
    byte KRYO = 3;
}
