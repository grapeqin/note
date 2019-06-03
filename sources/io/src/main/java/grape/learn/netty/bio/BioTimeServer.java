package grape.learn.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;

/**
 * 时间服务器Server端
 * @author grape
 * @date 2019-06-03
 */
public class BioTimeServer {

	public static void main(String[] args) throws IOException {
		new BioTimeServer().startServer(8080);
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
		//3. 等待客户端连接
		Socket client = null;
		while (true){
			client = serverSocket.accept();
			new Thread(new TimeServerHandler(client)).start();
		}

	}

	/**
	 * 时间服务器Server端逻辑处理器
	 */
	public static class TimeServerHandler implements Runnable{

		private final Socket socket;

		public TimeServerHandler(Socket socket){
			this.socket = socket;
		}

		@Override
		public void run() {
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				 PrintWriter printWriter = new PrintWriter(socket.getOutputStream())){
				//1.读取client 请求内容
				String msg = null;
				while((msg = bufferedReader.readLine()) != null){
					//3. 对读取到的内容进行解码
					String rsp = "BAD REQUEST!";
					System.out.println("client request msg : " + msg);
					if("QUERY SERVER TIME".equalsIgnoreCase(msg)){
						rsp = Instant.now().toString();
					}
					//3. 向客户端响应
					printWriter.println(rsp);
					printWriter.flush();
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
}
