package grape.learn.netty.codec;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 使用固定长度解码器FixedLengthFrameDecoder 实现EchoClient
 *
 * @author grape
 * @date 2019-06-06
 */
public class NettyFixedLengthEchoClient {
  /** 自定义包长度为20 */
  public static final int FIXEDLENGTH = 20;

  public static void main(String[] args) {
    new NettyFixedLengthEchoClient().startClient(1080);
  }

  /**
   * 启动client
   *
   * @param port
   */
  public void startClient(int port) {
    EventLoopGroup g = new NioEventLoopGroup();

    Bootstrap b = new Bootstrap();
    b.group(g)
        .channel(NioSocketChannel.class)
        .handler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                    .addLast(new FixedLengthFrameDecoder(FIXEDLENGTH))
                    .addLast(new StringDecoder())
                    .addLast(new EchoClientHandler());
              }
            });

    try {
      ChannelFuture cf = b.connect(new InetSocketAddress(port)).sync();

      cf.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      g.shutdownGracefully();
    }
  }

  /** EchoClient handler */
  private static class EchoClientHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      for (int i = 0; i < 10; i++) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Welcome to the world of Netty".getBytes()));
      }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
      System.out.println("server response msg : " + msg);
      assert ((String) msg).length() == FIXEDLENGTH;
    }
  }
}
