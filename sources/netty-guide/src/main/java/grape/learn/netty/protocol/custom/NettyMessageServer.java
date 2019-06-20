package grape.learn.netty.protocol.custom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

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
                    .addLast("timeoutHandler", new ReadTimeoutHandler(50))
                    .addLast("handsharkeHandler", new NettyMessageServerHandSharkeHandler())
                    .addLast("heartbeatHandler", new NettyMessageServerHeartbeatHandler())
                    .addLast("myHandler", new NettyMessageServerHandler());
              }
            });

    try {
      ChannelFuture future = b.bind(port).sync();
      System.out.println("Server has start successful!");

      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      g.shutdownGracefully();
      c.shutdownGracefully();
    }
  }
}
