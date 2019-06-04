## 基于TimeServer的各种IO实现示例

### 1. 基于BIO的实现

* Server 实现步骤 [BioTimeServer](src/main/java/grape/learn/netty/bio/BioTimeServer.java)

1. 创建ServerSocket
2. 调用accept方法循环获取已建立连接的socket
3. 启动新线程来处理Socket流的读写

* Client 实现步骤 [BioTimeClient](src/main/java/grape/learn/netty/bio/BioTimeClient.java)

1. 创建Socket
2. 向server发起connect
3. connect成功后处理Socket流的读写

### 2. 基于BIO的实现(伪异步IO)

* Server 实现步骤 [ForgeBioTimeServer](src/main/java/grape/learn/netty/bio/ForgeBioTimeServer.java)

1. 创建ServerSocket
2. 调用accept方法循环获取已建立连接的socket
3. 通过线程池来处理Socket流的读写

* Client 实现步骤 [BioTimeClient](src/main/java/grape/learn/netty/bio/BioTimeClient.java)

1. 参考基于BIO实现中的 Client 实现步骤

### 3. 基于NIO的实现

* Server 实现步骤 [NioTimeServer](src/main/java/grape/learn/netty/nio/NioTimeServer.java)

1. 创建多路复用器Selector,创建ServerSocketChannel
2. 设置ServerSocketChannel为异步Channel,并绑定到监听host和port
3. 将ServerSocketChannel 注册到 Selector
4. 执行Selector的select方法,并通过selectedKeys()获取就绪的Keys集合
5. 迭代SelectedKeys集合,此处注意**在迭代中务必将已处理的SelectedKey移除**
6. 如果是Accept事件，通过ServerSocketChannel的accept()方法获取接入的SocketChannel，向Selector注册READ事件
7. 如果是Read事件，表示接收到client发送的数据,构造ByteBuffer并通过SocketChannel的read方法将数据读到ByteBuffer进行业务处理
8. 根据业务需要通过SocketChannel向client回写数据

* Client 实现步骤 [NioTimeClient](src/main/java/grape/learn/netty/nio/NioTimeClient.java)

1. 创建多路复用器Selector，创建SocketChannel
2. 设置SocketChannel为异步Channel
3. SocketChannel发起connect请求，由于步骤2设置该Channel为异步，当前connect操作不会阻塞
4. 将SocketChannel的Connect事件注册到Selector
5. 执行Selector的select方法,并通过selectedKeys()获取就绪的Keys集合
6. 如果是Connect事件，判断连接是否建立完成，若建立好则向Server发送请求数据，并将SocketChannel的读事件注册到Selector，监听Server的响应数据
7. 如果是Read事件，借助ByteBuffer从SocketChannel读取Server响应数据完成业务逻辑

### 4. 基于AIO的实现

* 基于AIO的实现从代码逻辑来看 比NIO的代码要简略，但由于主要逻辑都是通过回调的形式来实现，结构上要复杂些

* Server 实现步骤 [AioTimeServer](src/main/java/grape/learn/netty/aio/AioTimeServer.java)

1. 创建AsynchronousServerSocketChannel 并绑定端口号
2. 直接开始执行AsynchronousServerSocketChannel的accept方法，传入回调函数;**注意:**方法执行是异步的，为了能保持主线程active，引入一个CountDownLatch来hang住主线程
3. 在AsynchronousServerSocketChannel的accept传入的回调函数中，务必参考API的示例，需要继续调用其accept()来接收下一个请求链接
4. 通过回调函数中的AsynchronousSocketChannel来接收client发送过来的数据并做业务逻辑处理,将响应结果回写给client

* Client 实现步骤 [AioTimeClient](src/main/java/grape/learn/netty/aio/AioTimeClient.java)

1. 创建AsynchronousSocketChannel 并与服务器建立连接
2. 在connect的回调函数中client向服务器写入请求数据
3. 通过AsynchronousSocketChannel 的read() 处理server的响应数据