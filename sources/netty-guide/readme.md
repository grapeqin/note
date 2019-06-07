* 以下所有示例均基于**netty-all.5.0.0.Alpha1**

## 基于Netty实现TimeServer(入门版)

* Server 实现步骤  [NettyTimeServer](src/main/java/grape/learn/netty/NettyTimeServer.java)

1. 新建EventLoopGroup
2. 新建ServerBootstrap,设置相关属性,最后一步通过childHandler 注册ChildChannelHandler
3. ServerBootstrap 绑定监听端口并同步等待直到绑定成功
4. ChannelFuture 注册端口关闭事件并同步等待
5. 基于ChildChannelHandler initial TimeServerHandler
6. 复写channelRead、channelReadComplete和exceptionCaught方法并完成相应业务逻辑

* Client 实现步骤  [NettyTimeClient](src/main/java/grape/learn/netty/NettyTimeClient.java)

1. 新建EventLoopGroup
2. 新建Bootstrap,设置属性,注册TimeClientHandler
3. 复写exceptionCaught、channelActive、channelRead完成相应业务逻辑

**Netty与Java NIO对比**

* 从代码量上来看，实现同样的业务逻辑，Netty在Server和Client实现中的编码量相比Java NIO都要少
* 从程序实现细节来看，Netty将NIO的很多细节都隐藏起来了，对应用开发者来说只需要重点关注自己的业务逻辑


***


<div id="stickunpackresolved"></div>

## 模拟TCP粘包问题的TimeServer实现

* Server 实现步骤 [StickUnpackNettyTimeServer](src/main/java/grape/learn/netty/StickUnpackNettyTimeServer.java)

* Client 实现步骤 [StickUnpackNettyTimeClient](src/main/java/grape/learn/netty/StickUnpackNettyTimeClient.java)

## 利用LineBasedFrameDecoder和StringDecoder 解决TCP粘包问题

* Server 实现步骤 [StickUnpackResolvedNettyTimeServer](src/main/java/grape/learn/netty/StickUnpackResolvedNettyTimeServer.java)

* Client 实现步骤 [StickUnpackResolvedNettyTimeClient](src/main/java/grape/learn/netty/StickUnpackResolvedNettyTimeClient.java)


## 分隔符DelimiterBasedFrameDecoder的使用

* Server 实现步骤 [NettyDelimiterEchoServer](src/main/java/grape/learn/netty/codec/NettyDelimiterEchoServer.java)

* Client 实现步骤 [NettyDelimiterEchoClient](src/main/java/grape/learn/netty/codec/NettyDelimiterEchoClient.java)

## 固定长度FixedLengthFrameDecoder的使用

* Server 实现步骤 [NettyFixedLengthEchoServer](src/main/java/grape/learn/netty/codec/NettyFixedLengthEchoServer.java)

* Client 实现步骤 [NettyFixedLengthEchoClient](src/main/java/grape/learn/netty/codec/NettyFixedLengthEchoClient.java)

**Netty解决TCP粘包/拆包问题**

* 提供了多种解码器来帮助业务开发人员避免TCP粘包/拆包问题,大大简化了程序开发,确保程序的正确性

***

<div id="codec-messagepack"></div>

## MessagePack 编解码实例

* MessagePack 示例 [MsgPackDemo](src/main/java/grape/learn/netty/codec/msgpack/MsgPackDemo.java)

* Server 实现步骤 [NettyMsgPackEchoServer](src/main/java/grape/learn/netty/codec/msgpack/NettyMsgPackEchoServer.java)

* Client 实现步骤 [NettyMsgPackEchoClient](src/main/java/grape/learn/netty/codec/msgpack/NettyMsgPackEchoClient.java)


## Protobuf 编解码实例

* ProtoBuf 示例

* Server 实现步骤

* Client 实现步骤