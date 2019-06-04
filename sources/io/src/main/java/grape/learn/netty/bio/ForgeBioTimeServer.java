package grape.learn.netty.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import grape.learn.netty.bio.BioTimeServer.TimeServerHandler;

/**
 * 伪异步I/O TimeServer implemention
 * @author grape
 * @date 2019-06-03
 */
public class ForgeBioTimeServer {

	public static void main(String[] args) throws IOException {
		new BioTimeServer().startServer(1080);
	}

	/**
	 * Server启动
	 * @throws IOException
	 */
	public void startServer(int port) throws IOException {
		//1. 创建ServerSocket
		ServerSocket serverSocket = new ServerSocket();

		//2. 设置Socket的一些option
		serverSocket.setReuseAddress(true);

		serverSocket.bind(new InetSocketAddress(port));
		System.out.println("ServerSocket has started!");

		// coreSize : 10
		// 容量有限的Queue:100
		// 拒绝策略：队列满之后，对于新接入的Socket，会无法处理
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10,10,5000, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());

		//3. 等待客户端连接
		Socket client = null;
		while (true){
			client = serverSocket.accept();
			poolExecutor.execute(new TimeServerHandler(client));
		}

	}
}
