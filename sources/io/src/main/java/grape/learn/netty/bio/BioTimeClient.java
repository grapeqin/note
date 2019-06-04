package grape.learn.netty.bio;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 时间服务器Client端
 * @author grape
 * @date 2019-06-03
 */
public class BioTimeClient {
	
	public static void main(String[] args){
		new BioTimeClient().startClient("localhost",1080);
	}

	/**
	 * 启动TimeServer的客户端
	 * @param host
	 * @param port
	 */
	public void startClient(String host,int port){
		//1.创建Socket
		Socket socket = null;
		PrintWriter printWriter =null;
		BufferedReader bufferedReader = null;
		try{
			//2. 向Server发起connect请求
			socket = new Socket();
			socket.connect(new InetSocketAddress(host,port));

			printWriter = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
			printWriter.println("QUERY SERVER TIME");
			printWriter.flush();

			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String msg = null;
			if ((msg = bufferedReader.readLine()) != null){
				System.out.println("server response : " + msg);
			}
		}catch (IOException e){
			e.printStackTrace();
		}finally {
			if(null!=printWriter){
				printWriter.close();
			}
			if(null!=bufferedReader){
				try {
					bufferedReader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(null!=socket){
				try {
					socket.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


	}
}
