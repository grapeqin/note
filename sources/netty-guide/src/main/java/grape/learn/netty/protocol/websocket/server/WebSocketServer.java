package grape.learn.netty.protocol.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * WebSocket Server 实现
 *
 * @author grape
 * @date 2019-06-13
 */
public class WebSocketServer {
  public static void main(String[] args) {
    new WebSocketServer().start(8080);
  }

  /**
   * 启动server
   *
   * @param port
   */
  public void start(int port) {
    EventLoopGroup g = new NioEventLoopGroup();
    EventLoopGroup c = new NioEventLoopGroup();
    ServerBootstrap b = new ServerBootstrap();
    b.group(g, c)
        .channel(NioServerSocketChannel.class)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                    .addLast(new HttpServerCodec())
                    .addLast(new HttpObjectAggregator(8176))
                    .addLast(new WebSocketServerHandshakerHandler())
                    .addLast(new WebSocketServerFrameHandler());
              }
            });
    try {
      b.bind(port).sync().channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      g.shutdownGracefully();
      c.shutdownGracefully();
    }
  }
}
