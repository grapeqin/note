### Chapter 1 : Java IO 复习

请参考 [基于4种IO实现方式的TimeServer](../sources/io/readme.md)

### Chapter 2 : Netty 入门

请参考 [Netty实现TimeServer](../sources/netty-guide/readme.md)

### Chapter 3 : TCP 粘包/拆包问题

请参考 [基于LineBasedFrameDecoder+StringDecoder解决TCP粘包问题](../sources/netty-guide/readme.md/#stickunpackresolved)

### Chapter 4 : 分隔符和定长解码器的应用

1. 分隔符解码器DelimiterBasedFrameDecoder应用实例

    - 请参考[基于DelimiterBasedFrameDecoder实现的EchoServer](../sources/netty-guide/readme.md/#stickunpackresolved)

2. 定长解码器FixedLengthFrameDecoder应用实例

    - 请参考[基于FixedLengthFrameDecoder实现的EchoServer](../sources/netty-guide/readme.md/#stickunpackresolved)
    
3. 消息长度解码器LengthFieldBasedFrameDecoder、编码器LengthFieldPrepender使用

   - 编码器 LengthFieldPrepender
   - 解码器 LengthFieldBasedFrameDecoder
    
### Chapter 5 : 编解码技术

1. Java 序列化
    
* Java序列化的用途
    - 网络传输
    - 对象持久化
    
* Java序列化的缺点
    - 无法跨语言
    - 序列化后的码流太大
    - 性能差 
    
2. 主流编解码框架介绍

* [MessagePack](../sources/netty-guide/readme.md/#codec-messagepack) 
* Google的Protobuf

### Chapter 6 ： HTTP协议开发应用

1. [Http静态文件服务器实例](../sources/netty-guide/readme.md/#protocol-http)

1. [Http+Xml简易订购实例](../sources/netty-guide/readme.md/#protocol-http) 

### Chapter 7 ：私有协议开发






