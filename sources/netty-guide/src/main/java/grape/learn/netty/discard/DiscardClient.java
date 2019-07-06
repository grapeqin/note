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
    System.err.println(
        "Usage: " + DiscardClient.class.getSimpleName() + " <host> <port> [<first message size>]");

    // Parse options.
    String host = "localhost";
    int port = 8080;
    int firstMessageSize = 256;
    if (args.length == 1) {
      host = args[0];
    } else if (args.length == 2) {
      host = args[0];
      port = Integer.parseInt(args[1]);
    } else if (args.length == 3) {
      host = args[0];
      port = Integer.parseInt(args[1]);
      firstMessageSize = Integer.parseInt(args[2]);
    }

    new DiscardClient(host, port, firstMessageSize).run();
  }
}
