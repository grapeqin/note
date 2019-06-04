package grape.learn.netty.nio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 基于NIO的 timeClient 实现
 * @author grape
 * @date 2019-06-03
 */
public class NioTimeClient {

	private volatile boolean stop;

	private static final String REQ = "QUERY SERVER TIME";

	public static void main(String[] args) {
		new NioTimeClient().startClient("localhost", 1080);
	}

	/**
	 * 启动NIO client
	 * @param host
	 * @param port
	 */
	public void startClient(String host, int port) {
		//1. 创建Selector和SocketChannel
		Selector selector = null;
		SocketChannel socketChannel = null;
		try{
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);

			//2. SocketChannel发起连接
			socketChannel.connect(new InetSocketAddress(host, port));
			//3. 将SocketChannel注册到Selector
			socketChannel.register(selector,SelectionKey.OP_CONNECT);

			while (!stop) {
				selector.select();
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				SelectionKey selectionKey;
				while (it.hasNext()){
					selectionKey = it.next();
					it.remove();
					if (selectionKey.isValid()) {
						SocketChannel channel = (SocketChannel) selectionKey.channel();
						if (selectionKey.isConnectable()) {
							//4.连接是否创建完成
							if(channel.finishConnect()){
								doWrite(socketChannel, REQ);
								channel.register(selector, SelectionKey.OP_READ);
							}else{
								System.exit(1);
							}
						}
						else if (selectionKey.isReadable()) {
							ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
							int length = channel.read(byteBuffer);
							if(length > 0){
								byteBuffer.flip();

								byte[] bytes = new byte[byteBuffer.remaining()];
								byteBuffer.get(bytes);
								String rsp = new String(bytes, "UTF-8");
								System.out.println("server rsp : " + rsp);
							}else if(length < 0){
								selector.close();
								socketChannel.close();
							}

						}
					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				selector.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			try {
				socketChannel.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		this.stop = true;
	}


	/**
	 * 向Server写数据
	 * @param socketChannel
	 * @param dest
	 */
	private void doWrite(SocketChannel socketChannel, String dest) {
		ByteBuffer buffer = ByteBuffer.allocate(dest.length());
		try {
			buffer.put(dest.getBytes("UTF-8"));
			buffer.flip();

			socketChannel.write(buffer);
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
			try {
				socketChannel.close();
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
