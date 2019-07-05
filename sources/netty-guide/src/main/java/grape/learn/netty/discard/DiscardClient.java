package grape.learn.netty.discard;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class DiscardClient {

  private final String host;
  private final int port;
  private final int firstMessageSize;

  public DiscardClient(String host, int port, int firstMessageSize) {
    this.host = host;
    this.port = port;
    this.firstMessageSize = firstMessageSize;
  }

  public void run() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
          .handler(new DiscardClientHandler(firstMessageSize));

      // Make the connection attempt.
      ChannelFuture f = b.connect(host, port).sync();

      // Wait until the connection is closed.
      f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws Exception {
    // Print usage if no argument is specified.
    if (args.length < 2 || args.length > 3) {
      System.err.println(
          "Usage: "
              + DiscardClient.class.getSimpleName()
              + " <host> <port> [<first message size>]");
      return;
    }

    // Parse options.
    final String host = args[0];
    final int port = Integer.parseInt(args[1]);
    final int firstMessageSize;
    if (args.length == 3) {
      firstMessageSize = Integer.parseInt(args[2]);
    } else {
      firstMessageSize = 256;
    }

    new DiscardClient(host, port, firstMessageSize).run();
  }
}
