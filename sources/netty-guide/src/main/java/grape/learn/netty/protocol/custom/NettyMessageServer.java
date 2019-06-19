package grape.learn.netty.protocol.custom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

/**
 * 自定义消息Server端实现
 *
 * @author grape
 * @date 2019-06-17
 */
public class NettyMessageServer {

  public static void main(String[] args) {
    new NettyMessageServer().run(8080);
  }

  /** @param port */
  public void run(int port) {
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
                    .addLast(
                        new MarshallingDecoder(MarshallerCodecFactory.createUnmarshallerProvider()))
                    .addLast(
                        new MarshallingEncoder(MarshallerCodecFactory.createMarshallerProvider()))
                    .addLast(new NettyMessageServerHandSharkeHandler())
                    .addLast(new NettyMessageServerHeartbeatHandler())
                    .addLast(new NettyMessageServerHandler());
              }
            });

    try {
      System.out.println("Server has start successful!");
      b.bind(port).sync().channel().closeFuture().sync();
    } catch (InterruptedException e) {
      g.shutdownGracefully();
      c.shutdownGracefully();
    }
  }
}
