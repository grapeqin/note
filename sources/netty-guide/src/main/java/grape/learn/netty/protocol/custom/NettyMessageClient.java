package grape.learn.netty.protocol.custom;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 自定义消息Client端实现
 *
 * <p>断连重连
 *
 * @author grape
 * @date 2019-06-17
 */
public class NettyMessageClient {

  ExecutorService executorService =
      new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

  EventLoopGroup g = new NioEventLoopGroup();

  public static void main(String[] args) {
    int port = 8080;
    if (args.length == 1) {
      port = Integer.parseInt(args[0]);
    }

    new NettyMessageClient().run(port);
  }

  public void run(int port) {
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
                    .addLast(new ReadTimeoutHandler(50))
                    .addLast(new NettyMessageClientHandSharkeHandler())
                    .addLast(new NettyMessageClientHeartbeatHandler())
                    .addLast(new NettyMessageClientHandler());
              }
            });

    try {
      ChannelFuture future = b.connect(new InetSocketAddress(port)).sync();
      if (future.isSuccess()) {
        System.out.println("client has connect successful");
      }

      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      // 发起重连操作
      executorService.execute(
          new Runnable() {
            @Override
            public void run() {
              try {
                Thread.sleep(5000);
                NettyMessageClient.this.run(port);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          });
    }
  }
}
