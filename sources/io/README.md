## 基于TimeServer的各种IO实现示例

### 1. 基于BIO的实现

* Server 实现步骤

1. 创建ServerSocket
2. 调用accept方法循环获取已建立连接的socket
3. 启动新线程来处理Socket流的读写

* Client 实现步骤

1. 创建Socket
2. 向server发起connect
3. connect成功后处理Socket流的读写

### 2. 基于NIO的实现

* Server 实现步骤



* Client 实现步骤


### 3. 基于AIO的实现


