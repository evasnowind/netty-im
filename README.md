# netty-im

本项目主要参考 [Netty 入门与实战：仿写微信 IM 即时通讯系统](https://juejin.im/book/6844733738119593991/section/6844733738270605326) ，
使用Netty框架仿写微信，以此深入学习Netty、网络编程知识。

对于编程，最好的学习方式就是造轮子！

# 技术实现
主要使用Netty来实现网络通讯，利用Netty ChannelPipeline机制，创建各种不同的handler，实现不同的IM业务逻辑。

## 关键技术点

| 功能                        | 实现                                                         | Netty技术                                                    | 备注                                                         |
| --------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 序列化/反序列               | PacketCodecHandler                                           | MessageToMessageCodec                                        |                                                              |
| 登录验证                    | AuthHandler                                                  | ChannelInboundHandlerAdapter                                 | 需要理解：ChannelPipeline机制，<br/>ChannelInbound/ChannelOutBound的区别，<br/>添加到pipeline中的顺序与处理顺序 |
| 构建pipeline                | NettyServer/NettyClient                                      | Bootstrap， ServerBootstrap                                  | Bootstrap，<br/> ServerBootstrap的pipeline如何设置，<br/>各种常用参数的配置 |
| 粘包/拆包处理               | Splitter                                                     | LengthFieldBasedFrameDecoder                                 | Netty自带拆包器的使用，<br/>LengthFieldBasedFrameDecoder如何使用 |
| 生命周期                    | LifeCircleTestHandler                                        | ChannelInboundHandlerAdapter/<br/>ChannelOutboundHandlerAdapter | 一个demo，务必要理解<br/>ChannelInboundHandlerAdapter/<br/>ChannelOutboundHandlerAdapter<br/>生命周期 |
| SimpleChannelInboundHandler | IMHandler，以及其他实现了<br/>SimpleChannelInboundHandler的类 | SimpleChannelInboundHandler                                  | SimpleChannelInboundHandler使用，<br/>如何使用Netty自带handler来简化程序设计。 |
| ChannelGroup群组管理        | CreateGroupRequestHandler<br/>GroupMessageRequestHandler<br/>JoinGroupRequestHandler等 | ChannelGroup                                                 | 使用ChannelGroup管理群组，发送群组消息等                     |
| 心跳与空闲检查              | IMIdleStateHandler<br/>HeartBeatTimerHandler<br/>HeartBeatRequestHandler | IdleStateHandler                                             | Netty如何进行心跳与空闲检查                                  |
|                             |                                                              |                                                              |                                                              |



## 使用

1、运行NettyServer，启动IM服务端

2、启动NettyClient客户端，填写用户名

3、输入命令，执行各种IM相关操作，目前支持如下操作：

	- sendToUser : 发送消息给指定用户（模拟私聊）
	- logout ：登出，断开与服务端的链接
	- createGroup : 创建群组
	- joinGroup : 加入指定群组
	- quitGroup : 退出群组
	- listGroupMembers : 列出指定群组内所有用户
	- sendToGroup : 发送消息给指定群组（群发）

# 参考资料
- [Netty 入门与实战：仿写微信 IM 即时通讯系统](https://juejin.im/book/6844733738119593991/section/6844733738270605326)