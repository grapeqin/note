package grape.learn.netty.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;

/**
 * 基于异步I/O的 TimeServer 实现
 * @author grape
 * @date 2019-06-04
 */
public class AioTimeServer {

	private static final String REQ = "QUERY SERVER TIME";

	public static void main(String[] args){
		new AioTimeServer().startServer(1080);
	}

	/**
	 * 启动Server
	 * @param port
	 */
	public void startServer(int port){
		CountDownLatch countDownLatch = new CountDownLatch(1);

		//1.创建AsynchronousServerSocketChannel
		AsynchronousServerSocketChannel serverSocketChannel = null;
		try{
			serverSocketChannel = AsynchronousServerSocketChannel.open();
			//2.绑定端口号
			serverSocketChannel.bind(new InetSocketAddress(port));

			//3.等待客户端建立连接,建立成功后则直接处理请求
			doAccept(serverSocketChannel);

			// 由于请求都是异步的，这里不hang住线程 主进程直接就退出了
			countDownLatch.await();
		}catch (IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			try {
				serverSocketChannel.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理接入事件
	 * @param serverSocketChannel
	 */
	public void doAccept(AsynchronousServerSocketChannel serverSocketChannel){
		serverSocketChannel.accept(serverSocketChannel, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
			@Override
			public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
				System.out.println("Server has geted a new connection");
				//4. 等待下一个连接到来
				attachment.accept(attachment,this);

				handleAcceptRequest(result);
			}

			@Override
			public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
				//TODO

			}
		});
	}

	/**
	 * server 处理新连接的接入
	 * @param socketChannel
	 */
	private void handleAcceptRequest(AsynchronousSocketChannel socketChannel){
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

		// 读取客户端请求过来的数据
		socketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
			@Override
			public void completed(Integer result, ByteBuffer attachment) {
				if(result > 0){
					// 读取到数据
					byteBuffer.flip();
					assert result == attachment.remaining();
					byte[] bytes = new byte[result];
					byteBuffer.get(bytes);
					try {
						String req = new String(bytes,"UTF-8");
						System.out.println("client send req:" + req);
						String rsp = "BAD REQUEST!";
						if(REQ.equalsIgnoreCase(req)){
							rsp = Instant.now().toString();
							writeResponseToClient(socketChannel,rsp);
						}
					}
					catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}else if(result < 0){
					try {
						socketChannel.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				//TODO
			}
		});
	}

	/**
	 * 向AsynchronousSocketChannel 写响应
	 * @param socketChannel
	 * @param rspMsg
	 */
	private void writeResponseToClient(AsynchronousSocketChannel socketChannel,String rspMsg){
		ByteBuffer byteBuffer = ByteBuffer.allocate(rspMsg.getBytes().length);
		try {
			byteBuffer.put(rspMsg.getBytes("UTF-8"));
			byteBuffer.flip();
			socketChannel.write(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
				@Override
				public void completed(Integer result, ByteBuffer attachment) {
					if(byteBuffer.hasRemaining()){
						socketChannel.write(byteBuffer,byteBuffer,this);
					}
					System.out.println("server finish write to client.");
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					//TODO
				}
			});
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
