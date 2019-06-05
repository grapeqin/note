package grape.learn.netty.nio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

/**
 * 基于NIO的 TimeServer 实现
 * @author grape
 * @date 2019-06-03
 */
public class NioTimeServer {

	private volatile boolean stop = false;

	private static final String REQ = "QUERY SERVER TIME";
	
	public static void main(String[] args){
		new NioTimeServer().startServer(1080);
	}

	/**
	 * 启动NIO Server
	 * @param port
	 */
	public void startServer(int port){
		try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			 Selector selector = Selector.open()){
			//1.创建ServerSocketChannel 并绑定
			//2.创建多路复用器Selector
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(port));
			System.out.println("serverSocketChannel bind success!");

			//3.将ServerSocketChannel 注册到 Selector
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("serverSocketChannel register to selector success!");

			//4.遍历selector.select
			Set<SelectionKey> selectionKeys;

			while (!stop){
				selector.select();
				selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectionKeys.iterator();
				SelectionKey key = null;
				while (it.hasNext()){
					//5. key 有效 则进行业务处理
					key = it.next();
					it.remove();
					if(key.isValid()){
						handleKey(key);
					}
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * 业务逻辑处理
	 * @param key
	 */
	private void handleKey(SelectionKey key){
		//1.如果是新连接,则注册READ事件
		if(key.isAcceptable()){
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
			try {
				SocketChannel socketChannel = serverSocketChannel.accept();
				socketChannel.configureBlocking(false);

				socketChannel.register(key.selector(),SelectionKey.OP_READ);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}else if(key.isReadable()){
			//2.如果是读事件，表示客户端发上来的请求数据
			SocketChannel socketChannel = (SocketChannel) key.channel();
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			try {
				int length = socketChannel.read(byteBuffer);
				if(length > 0){
					//1.读取到内容了
					byteBuffer.flip();

					byte[] body = new byte[byteBuffer.remaining()];
					byteBuffer.get(body);

					String req = new String(body,"UTF-8");
					System.out.println("server has received: " + req);

					String rsp = "BAD REQUEST!";
					if(REQ.equalsIgnoreCase(req)){
						rsp = LocalDateTime.now().toString();
					}
					doWrite(socketChannel,rsp);
				}else if(length < 0){
					key.cancel();
					socketChannel.close();
				}else{

				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 向客户端回复消息
	 * @param socketChannel
	 * @param rsp
	 */
	private void doWrite(SocketChannel socketChannel,String rsp){
		ByteBuffer byteBuffer = ByteBuffer.allocate(rsp.length());
		try {
			byteBuffer.put(rsp.getBytes("UTF-8"));
			byteBuffer.flip();

			socketChannel.write(byteBuffer);
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop(){
		this.stop = true;
	}
}
