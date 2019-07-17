package grape.learn.netty.factorial;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.TrustManagerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

/**
 * Sends a sequence of integers to a {@link FactorialServer} to calculate the factorial of the
 * specified integer.
 */
public final class FactorialClient {

  static final boolean SSL = System.getProperty("ssl") != null;
  static final String HOST = System.getProperty("host", "127.0.0.1");
  static final int PORT = Integer.parseInt(System.getProperty("port", "8322"));
  static final int COUNT = Integer.parseInt(System.getProperty("count", "4099"));

  public static void main(String[] args) throws Exception {
    // Configure SSL.
    final SslContext sslCtx;
    if (SSL) {
      // 1.使用自己生成的服务端证书并导入到客户端的信任仓库中
      String clientTrustKeyStoreFile = "/Users/qinzy/mynettyclienttrust.keystore";
      String clientTrustKeyPass = "nettyclient";

      KeyStore clientTrustKeyStore = KeyStore.getInstance("JKS");
      clientTrustKeyStore.load(
          new FileInputStream(clientTrustKeyStoreFile), clientTrustKeyPass.toCharArray());

      TrustManagerFactory tmf =
          TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      tmf.init(clientTrustKeyStore);

      sslCtx = SslContextBuilder.forClient().trustManager(tmf).build();
    } else {
      sslCtx = null;
    }

    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
          .handler(new FactorialClientInitializer(sslCtx));

      // Make a new connection.
      ChannelFuture f = b.connect(HOST, PORT).sync();

      // Get the handler instance to retrieve the answer.
      FactorialClientHandler handler = (FactorialClientHandler) f.channel().pipeline().last();

      // Print out the answer.
      System.err.format("Factorial of %,d is: %,d", COUNT, handler.getFactorial());
    } finally {
      group.shutdownGracefully();
    }
  }
}
