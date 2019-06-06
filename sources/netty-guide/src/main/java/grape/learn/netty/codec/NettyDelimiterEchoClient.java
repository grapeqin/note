package grape.learn.netty.codec;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 使用分隔符解码器来 处理TCP粘包/拆包问题 的EchoClient
 *
 * @author grape
 * @date 2019-06-05
 */
public class NettyDelimiterEchoClient {
  /** 分隔符 */
  public static final String DELIMITER = "$_";

  public static void main(String[] args) {
    new NettyDelimiterEchoClient().startClient(1080);
  }

  /**
   * 启动EchoClient
   *
   * @param port
   */
  public void startClient(int port) {
    EventLoopGroup loopGroup = new NioEventLoopGroup();

    Bootstrap bootstrap = new Bootstrap();
    bootstrap
        .group(loopGroup)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY, true)
        .handler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                    .addLast(
                        new DelimiterBasedFrameDecoder(
                            1024, Unpooled.copiedBuffer(DELIMITER.getBytes())))
                    .addLast(new StringDecoder())
                    .addLast(new EchoClientHandler());
              }
            });

    try {
      ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(port)).sync();

      channelFuture.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      loopGroup.shutdownGracefully();
    }
  }

  /** EchoClient handler处理类 */
  private static class EchoClientHandler extends ChannelHandlerAdapter {
    private int counter;
    private final String msg = "Hello grapeQin,Welcome to the world of Netty." + DELIMITER;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      for (int i = 0; i < 10; i++) {
        ctx.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
      }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      String messgge = (String) msg;
      System.out.println("client receive message :" + messgge + ",counter:" + ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }
  }
}
