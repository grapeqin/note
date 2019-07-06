* 以下所有示例均基于**netty-all.4.1.37.Final**

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

***

## 多协议开发与应用

<div id="protocol-http"></div>

### HTTP协议开发与应用

#### 一、Http协议开发的静态文件服务器

  [HttpStaticFileServer](src/main/java/grape/learn/netty/protocol/http/file/HttpStaticFileServer.java)
  
#### 二、Http+XML开发的简易订购服务 

##### 1、XML处理工具类[jibx](http://jibx.sourceforge.net/getting-started.html)

1. 编写Object类，请参考[Order](src/main/java/grape/learn/netty/protocol/http/xml/Order.java)、Customer、Address和Shipping
1. 安装Jibx，从[SourceForge downloads page](https://sourceforge.net/projects/jibx/files/)下载最新的jibx版本，解压缩到/usr/local/jibx；并配置环境变量JIBX_HOME=/usr/local/jibxClass生成binding.xml和xsd文件，请参考[bindgen](http://jibx.sourceforge.net/fromcode/bindgen.html)利用[bindgen](http://jibx.sourceforge.net/fromcode/bindgen.html)来基于source code 生成bingding.xml和xsd文件：参考[build.xml](build.xml)
1. 配置ant中bindgen和bind 两个task；依次执行bindgen和bind两个任务，保证任务执行无错误，如有错误，请根据错误信息排查问题并改进
1. 到项目根目录[pom.xml](pom.xml)中增加jibx runtime相关的依赖jibx-run
1. 创建[TestOrder](src/main/java/grape/learn/netty/protocol/http/xml/TestOrder.java)，在IDE中执行TestOrder时请**务必在Run Configuration中的Build中添加Ant中的bind任务**
    
##### 2、Http+XML的服务示例

###### Quick Start
```jshelllanguage
mvn clean compile exec:java -Dexec.mainClass="grape.learn.netty.protocol.http.xml.order.NettyHttpXmlOrderServer"

mvn exec:java -Dexec.mainClass="grape.learn.netty.protocol.http.xml.order.NettyHttpXmlOrderClient"
```
  
###### Client发送请求 Server接收请求实现

1. 协议设计：为保持业务层面对Http的扩展性，我们创建HttpXmlRequest对象来承载业务对象
1. Client端发送数据，从pipeline协议编码时序图来说，首先需要自定义编码器HttpXmlEncoder将HttpXmlRequest对象编码为FullHttpRequest对象，然后使用Netty提供的HttpEncoder将FullHttpRequest对象编码为字节码
1. Server端接收数据，从pipeline协议解析时序图来说，首先需要借助于HttpRequestDecoder和HttpObjectAggregator将字节流解码为FullHttpRequest对象，然后使用我们自定义的HttpXmlDecoder解码器将FullHttpRequest对象解码为HttpXmlRequest对象
1. 依次创建并启动NettyHttpXmlOrderServer和NettyHttpXmlOrderClient
    
###### Server接收请求后返回消息 Client接收返回消息实现

1. 协议设计：参考HttpXmlRequest设计思路，创建HttpXmlResponse对象
1. Server端回复数据，从pipeline协议编码时序图来说，首先需要自定义编码器HttpXmlResponseEncoder将HttpXmlResponse对象编码为FullHttpResponse对象，然后使用Netty提供的HttpEncoder将FullHttpResponse对象编码为字节码
1. Client端接收响应数据，从pipeline协议解码时序图来说，首先需要借助于HttpRequestDecoder和HttpObjectAggregator将字节流解码为FullHttpResponse对象，然后使用自定义解码器HttpXmlResponseDecoder将FullHttpResponse对象解码为HttpXmlResponse对象，供应用层使用
1. 依次在NettyHttpXmlOrderServer和NettyHttpXmlOrderClient 中追加以上自定义的编解码器，并补充完善业务Handler

#### 三、WebSocket 协议开发

##### 1、[WebSocket协议基础](https://www.websocket.org/aboutwebsocket.html)

#### 四、私有协议开发

- 开发步骤

1. 私有消息定义 Header、NettyMessage
2. 消息编解码支持 MarshallerCodecFactory
3. 简易Client、Server，完成Client问Server应答的基本功能，并验证编解码器是否如预期工作
4. 握手认证 client端建立完连接后 向server发送握手请求消息，server端完成登录和白名单校验后，返回握手成功消息
5. 心跳检测 client端在完成与server端的握手后，定时每5S向server发送心跳请求消息并接受服务端的心跳响应，server端接收到心跳请求后构造心跳响应回复client端
6. client端失败重连

- Q1：NettyMessageServerHandSharkeHandler 代码中
想通过成员变量存储服务端所有的登录认证信息，但发现每个新的client过来时 都会new一个新的实例导致结果与预期不符.
- 通过在类NettyMessageServerHandSharkeHandler添加Sharable注解，即可实现单实例在多个连接中共用，达到单实例管理多连接状态的目标

#### 五、源码分析

* ByteBuf和相关辅助类

1. 顺序read
1. 顺序write
1. readerIndex和writerIndex
1. readableBytes和writeableBytes
1. discardBytes
1. clear
- 操作readerIndex = writerIndex = 0
1. Mark 和 Rest
1. 查找操作
1. Derived buffers
1. 转换成标准的ByteBuffer
1. 随机读写(set 和 get)

* ByteBuf源码分析

分类

1. 从内存分配角度看 分为 HeapByteBuf和DirectByteBuf
1. 从内存回收角度看 分为 对象池ByteBuf和普通ByteBuf









