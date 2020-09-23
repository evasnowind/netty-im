package com.prayerlaputa.im.study.server;

import com.prayerlaputa.im.study.codec.PacketDecoder;
import com.prayerlaputa.im.study.codec.PacketEncoder;
import com.prayerlaputa.im.study.codec.Splitter;
import com.prayerlaputa.im.study.server.handler.AuthHandler;
import com.prayerlaputa.im.study.server.handler.CreateGroupRequestHandler;
import com.prayerlaputa.im.study.server.handler.JoinGroupRequestHandler;
import com.prayerlaputa.im.study.server.handler.ListGroupMembersRequestHandler;
import com.prayerlaputa.im.study.server.handler.LoginRequestHandler;
import com.prayerlaputa.im.study.server.handler.LogoutRequestHandler;
import com.prayerlaputa.im.study.server.handler.MessageRequestHandler;
import com.prayerlaputa.im.study.server.handler.QuitGroupRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Date;


public class NettyServer {

    private static final int BEGIN_PORT = 8000;
    private static final int PORT = 8000;

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
                        //利用Netty提供的ChannelPipeline机制，重写
//                        ch.pipeline().addLast(new FirstServerHandler());
                        ch.pipeline()
//                                .addLast(new LifeCircleTestHandler())
                                .addLast(new Splitter())
                                .addLast(new PacketDecoder())
                                // 登录请求处理器
                                .addLast(new LoginRequestHandler())
                                //鉴权
                                .addLast(new AuthHandler())
                                // 单聊消息请求处理器
                                .addLast(new MessageRequestHandler())
                                // 创建群请求处理器
                                .addLast(new CreateGroupRequestHandler())
                                // 加群请求处理器
                                .addLast(new JoinGroupRequestHandler())
                                // 退群请求处理器
                                .addLast(new QuitGroupRequestHandler())
                                // 获取群成员请求处理器
                                .addLast(new ListGroupMembersRequestHandler())
                                // 登出请求处理器
                                .addLast(new LogoutRequestHandler())
                                .addLast(new PacketEncoder());
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
