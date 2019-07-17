package grape.learn.netty.factorial;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

/**
 * Receives a sequence of integers from a {@link FactorialClient} to calculate the factorial of the
 * specified integer.
 */
public final class FactorialServer {

  static final boolean SSL = System.getProperty("ssl") != null;
  static final int PORT = Integer.parseInt(System.getProperty("port", "8322"));

  public static void main(String[] args) throws Exception {
    // Configure SSL.
    final SslContext sslCtx;
    if (SSL) {
      // 1.使用自己生成的服务端证书来完成SSL的安全传输
      String serverKeyStoreFile = "/Users/qinzy/mynettyserver";
      String serverKeyStorePass = "nettyserver";
      String serverKeyPwd = "nettyserver";

      KeyStore serverKeyStore = KeyStore.getInstance("JKS");
      serverKeyStore.load(
          new FileInputStream(serverKeyStoreFile), serverKeyStorePass.toCharArray());

      KeyManagerFactory kmf =
          KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      kmf.init(serverKeyStore, serverKeyPwd.toCharArray());

      sslCtx = SslContextBuilder.forServer(kmf).build();
    } else {
      sslCtx = null;
    }

    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .handler(new LoggingHandler(LogLevel.INFO))
          .childHandler(new FactorialServerInitializer(sslCtx));

      b.bind(PORT).sync().channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}
