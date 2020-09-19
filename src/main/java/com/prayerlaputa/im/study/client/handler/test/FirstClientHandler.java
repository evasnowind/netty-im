package com.prayerlaputa.im.study.client.handler.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author chenglong.yu
 * created on 2020/9/17
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {


    /**
     * 在客户端连接建立成功之后被调用
     *
     * @param context
     */
    @Override
    public void channelActive(ChannelHandlerContext context) {
        System.out.println(new Date() + ": 客户端写出数据");

        //1. 获取数据
        ByteBuf buffer = getByteBuf(context);
        //2. 写数据
        context.channel()
                .writeAndFlush(buffer);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext context) {
        byte[] strBytes = "Hello, world!".getBytes(StandardCharsets.UTF_8);
        /*
        Netty 里面数据是以 ByteBuf 为单位的， 所有需要写出的数据都必须塞到一个 ByteBuf，数据的写出是如此，数据的读取亦是如此
         */
        ByteBuf byteBuffer = context.alloc().buffer();
        byteBuffer.writeBytes(strBytes);
        return byteBuffer;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date() + ": 客户端读到数据 -> " + byteBuf.toString(StandardCharsets.UTF_8));
    }
}
