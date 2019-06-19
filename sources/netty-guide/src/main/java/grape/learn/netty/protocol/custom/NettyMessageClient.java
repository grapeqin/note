package grape.learn.netty.protocol.custom;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

/**
 * 自定义消息Client端实现
 *
 * @author grape
 * @date 2019-06-17
 */
public class NettyMessageClient {
  public static void main(String[] args) {
    new NettyMessageClient().run(8080);
  }

  public void run(int port) {
    EventLoopGroup g = new NioEventLoopGroup();
    Bootstrap b = new Bootstrap();

    MarshallerProvider marshallerProvider = MarshallerCodecFactory.createMarshallerProvider();
    UnmarshallerProvider unmarshallerProvider = MarshallerCodecFactory.createUnmarshallerProvider();
    MarshallingEncoder marshallingEncoder = new MarshallingEncoder(marshallerProvider);
    MarshallingDecoder marshallingDecoder = new MarshallingDecoder(unmarshallerProvider);

    b.group(g)
        .channel(NioSocketChannel.class)
        .handler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                    .addLast(marshallingEncoder)
                    .addLast(marshallingDecoder)
                    .addLast(new NettyMessageClientHandSharkeHandler())
                    .addLast(new NettyMessageClientHeartbeatHandler())
                    .addLast(new NettyMessageClientHandler());
              }
            });

    try {
      System.out.println("client has connect successful!");
      b.connect(new InetSocketAddress(port)).sync().channel().closeFuture().sync();
    } catch (InterruptedException e) {
      g.shutdownGracefully();
    }
  }
}
