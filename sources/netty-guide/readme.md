## 基于Netty实现TimeServer(入门版)

* 示例基于**netty-all.5.0.0.Alpha1**

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