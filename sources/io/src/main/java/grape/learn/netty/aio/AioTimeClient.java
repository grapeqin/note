package grape.learn.netty.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * 基于异步I/O的 TimeClient 实现
 * @author grape
 * @date 2019-06-04
 */
public class AioTimeClient {

	private static final String REQ = "QUERY SERVER TIME";

	public static void main(String[] args){
		new AioTimeClient().startClient(1080);
	}

	/**
	 * AIO TimeServer bootstrap
	 * @param port
	 */
	public void startClient(int port){
		AsynchronousSocketChannel socketChannel = null;
		try {
			//1. 创建AsynchronousSocketChannel
			socketChannel = AsynchronousSocketChannel.open();
			socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR,true);

			CountDownLatch countDownLatch = new CountDownLatch(1);
			//2. 连接server
			socketChannel.connect(new InetSocketAddress(port), socketChannel, new CompletionHandler<Void, AsynchronousSocketChannel>() {
				@Override
				public void completed(Void result, AsynchronousSocketChannel attachment) {
					doWrite(attachment);
				}

				@Override
				public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
					//TODO
				}
			});

			countDownLatch.await();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			try {
				socketChannel.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 向服务器端发送数据
	 * @param socketChannel
	 */
	private void doWrite(AsynchronousSocketChannel socketChannel){
		try {
			byte[] body = REQ.getBytes("UTF-8");
			ByteBuffer byteBuffer = ByteBuffer.allocate(body.length);
			byteBuffer.put(body);
			byteBuffer.flip();
			socketChannel.write(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
				@Override
				public void completed(Integer result, ByteBuffer attachment) {
					if(attachment.hasRemaining()){
						socketChannel.write(byteBuffer,byteBuffer,this);
					}else {
						doRead(socketChannel);
					}
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

	/**
	 * 从服务器端读取数据
	 * @param socketChannel
	 */
	private void doRead(AsynchronousSocketChannel socketChannel){
		ByteBuffer src = ByteBuffer.allocate(1024);
		socketChannel.read(src, socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
			@Override
			public void completed(Integer result, AsynchronousSocketChannel attachment) {
				src.flip();
				byte[] body = new byte[src.remaining()];
				src.get(body);
				try {
					String msg = new String(body,"UTF-8");
					System.out.println("client received msg: " + msg);
				}
				catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
				//TODO
			}
		});
	}
}
