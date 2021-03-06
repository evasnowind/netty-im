package com.prayerlaputa.im.study.client;

import com.prayerlaputa.im.study.client.console.ConsoleCommandManager;
import com.prayerlaputa.im.study.client.console.LoginConsoleCommand;
import com.prayerlaputa.im.study.client.handler.CreateGroupResponseHandler;
import com.prayerlaputa.im.study.client.handler.GroupMessageResponseHandler;
import com.prayerlaputa.im.study.client.handler.HeartBeatTimerHandler;
import com.prayerlaputa.im.study.client.handler.JoinGroupResponseHandler;
import com.prayerlaputa.im.study.client.handler.ListGroupMembersResponseHandler;
import com.prayerlaputa.im.study.client.handler.LoginResponseHandler;
import com.prayerlaputa.im.study.client.handler.LogoutResponseHandler;
import com.prayerlaputa.im.study.client.handler.MessageResponseHandler;
import com.prayerlaputa.im.study.client.handler.QuitGroupResponseHandler;
import com.prayerlaputa.im.study.codec.PacketDecoder;
import com.prayerlaputa.im.study.codec.PacketEncoder;
import com.prayerlaputa.im.study.codec.Splitter;
import com.prayerlaputa.im.study.handler.IMIdleStateHandler;
import com.prayerlaputa.im.study.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    public static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 18000;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 1.指定线程模型
                .group(workerGroup)
                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)

                /*
                attr() 方法可以给客户端 Channel，也就是NioSocketChannel绑定自定义属性，然后我们可以通过channel.attr()取出这个属性
                 */
                .attr(AttributeKey.newInstance("clientName"), "nettyClient")
                /*
                给连接设置一些 TCP 底层相关的属性。
                ChannelOption.CONNECT_TIMEOUT_MILLIS 表示连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                ChannelOption.SO_KEEPALIVE 表示是否开启 TCP 底层心跳机制，true 为开启
                ChannelOption.TCP_NODELAY 表示是否开始 Nagle 算法，true 表示关闭，false 表示开启，通俗地说，如果要求高实时性，
                有数据发送时就马上发送，就设置为 true 关闭，如果需要减少发送次数减少网络交互，就设置为 false 开启
                 */
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                // 3.IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        //利用Netty的channelPipeline机制重写
//                        ch.pipeline().addLast(new FirstClientHandler());
                        ch.pipeline()
                                .addLast(new IMIdleStateHandler())
                                .addLast(new Splitter())
                                .addLast(new PacketDecoder())
                                // 登录响应处理器
                                .addLast(new LoginResponseHandler())
                                // 收消息处理器
                                .addLast(new MessageResponseHandler())
                                // 创建群响应处理器
                                .addLast(new CreateGroupResponseHandler())
                                // 加群响应处理器
                                .addLast(new JoinGroupResponseHandler())
                                // 退群响应处理器
                                .addLast(new QuitGroupResponseHandler())
                                // 获取群成员响应处理器
                                .addLast(new ListGroupMembersResponseHandler())
                                // 群消息响应
                                .addLast(new GroupMessageResponseHandler())
                                // 登出响应处理器
                                .addLast(new LogoutResponseHandler())
                                .addLast(new PacketEncoder())
                                .addLast(new HeartBeatTimerHandler());

                    }
                });

        // 4.建立连接
        connect(bootstrap, HOST, PORT, MAX_RETRY);

    }

    /**
     * 实现指数退避的客户端重连逻辑
     * <p>
     * 如果连接成功则打印连接成功的消息
     * 如果连接失败但是重试次数已经用完，放弃连接
     * 如果连接失败但是重试次数仍然没有用完，则计算下一次重连间隔 delay，然后定期重连
     *
     * @param bootstrap
     * @param host
     * @param port
     * @param retry
     */
    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                /*
                通常情况下，连接建立失败不会立即重新连接，而是会通过一个指数退避的方式，
                比如每隔 1 秒、2 秒、4 秒、8 秒，以 2 的幂次来建立连接，
                然后到达一定次数之后就放弃连接，接下来我们就来实现一下这段逻辑，我们默认重试 5 次
                * */
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!SessionUtil.hasLogin(channel)) {
                    loginConsoleCommand.exec(scanner, channel);
                } else {
                    consoleCommandManager.exec(scanner, channel);
                }
            }
        }).start();
    }

    private static void waitForLoginResponse() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
