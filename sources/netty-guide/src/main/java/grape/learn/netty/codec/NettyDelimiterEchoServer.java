package grape.learn.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 使用分隔符解码器来 处理TCP粘包/拆包问题 的EchoServer
 *
 * @author grape
 * @date 2019-06-05
 */
public class NettyDelimiterEchoServer {

  /** 分隔符定义 */
  public static final String DELIMITER = "$_";

  public static void main(String[] args) {
    new NettyDelimiterEchoServer().startEchoServer(1080);
  }

  /**
   * 启动EchoServer
   *
   * @param port
   */
  public void startEchoServer(int port) {
    // 1.创建EventLoopGroup
    EventLoopGroup parent = new NioEventLoopGroup();
    EventLoopGroup child = new NioEventLoopGroup();

    // 2.创建ServerBootstrap
    ServerBootstrap bp = new ServerBootstrap();
    bp.group(parent, child)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 1024)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                    .addLast(
                        new DelimiterBasedFrameDecoder(
                            1024, Unpooled.copiedBuffer(DELIMITER.getBytes())))
                    .addLast(new StringDecoder())
                    .addLast(new EchoServerHandler());
              }
            });

    try {
      ChannelFuture channelFuture = bp.bind(port).sync();

      channelFuture.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      parent.shutdownGracefully();
      child.shutdownGracefully();
    }
  }

  /** 处理业务逻辑的handler */
  private static class EchoServerHandler extends ChannelHandlerAdapter {

    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      String message = (String) msg;
      System.out.println("server has received message:" + message + ",counter:" + ++counter);

      // 由于采用了delimiter解码器，应用层消息必须包含该分隔符，否则解码层会有问题
      message += DELIMITER;
      ctx.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
    }
  }
}
