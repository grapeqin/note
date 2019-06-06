package grape.learn.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 使用固定长度解码器FixedLengthFrameDecoder 实现EchoServer
 *
 * @author grape
 * @date 2019-06-06
 */
public class NettyFixedLengthEchoServer {
  /** 自定义包长度为20 */
  public static final int FIXEDLENGTH = 20;

  public static void main(String[] args) {
    new NettyFixedLengthEchoServer().startServer(1080);
  }

  /**
   * 启动EchoServer
   *
   * @param port
   */
  public void startServer(int port) {
    EventLoopGroup group = new NioEventLoopGroup();
    EventLoopGroup childGroup = new NioEventLoopGroup();

    ServerBootstrap b = new ServerBootstrap();
    b.group(group, childGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                    .addLast(new FixedLengthFrameDecoder(FIXEDLENGTH))
                    .addLast(new StringDecoder())
                    .addLast(new EchoServerHandler());
              }
            });

    try {
      ChannelFuture cf = b.bind(port).sync();

      cf.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      group.shutdownGracefully();
      childGroup.shutdownGracefully();
    }
  }

  private static class EchoServerHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      System.out.println("server received msg : " + msg);
      String body = (String) msg;
      ctx.writeAndFlush(Unpooled.copiedBuffer(body.getBytes()));
      assert body.length() == FIXEDLENGTH;
    }
  }
}
