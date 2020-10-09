package com.prayerlaputa.im.study.server;

import com.prayerlaputa.im.study.codec.PacketCodecHandler;
import com.prayerlaputa.im.study.codec.Splitter;
import com.prayerlaputa.im.study.handler.IMIdleStateHandler;
import com.prayerlaputa.im.study.server.handler.AuthHandler;
import com.prayerlaputa.im.study.server.handler.HeartBeatRequestHandler;
import com.prayerlaputa.im.study.server.handler.IMHandler;
import com.prayerlaputa.im.study.server.handler.LoginRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Date;


public class NettyServer {

    private static final int PORT = 18000;

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) {
                        /*
                        childHandler()用于指定处理新连接数据的读写处理逻辑，handler()用于指定在服务端启动过程中的一些逻辑，通常情况下呢，我们用不着这个方法。
                         */
                        System.out.println(new Date() + ": 服务端启动中");
                    }
                })

                /*
                指定自定义属性，一般也用不到这个。
                */
                .attr(AttributeKey.newInstance("serverName"), "nettyServer")
                /*
                给每个连接指定自定义属性。
                后续我们可以通过channel.attr()取出该属性。
                 */
                .childAttr(AttributeKey.newInstance("clientKey"), "clientValue")
                /*
                       给服务端channel设置一些属性，最常见的就是so_backlog，如下设置.
                       表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数。
                        */
                .option(ChannelOption.SO_BACKLOG, 1024)
                /*
                childOption()可以给每条连接设置一些TCP底层相关的属性。如下面设置了两种TCP属性：
                ChannelOption.SO_KEEPALIVE表示是否开启TCP底层心跳机制，true为开启
                ChannelOption.TCP_NODELAY表示是否开启Nagle算法，true表示关闭，false表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启。
                 */
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline()
                                .addLast(new IMIdleStateHandler())
                                .addLast(new Splitter())
                                .addLast(PacketCodecHandler.INSTANCE)
                                // 登录请求处理器
                                .addLast(LoginRequestHandler.INSTANCE)
                                .addLast(HeartBeatRequestHandler.INSTANCE)
                                .addLast(AuthHandler.INSTANCE)
                                //此处通过IMHandler缩短事件传播路径
                                .addLast(IMHandler.INSTANCE);
                    }
                });

        bind(serverBootstrap, PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
