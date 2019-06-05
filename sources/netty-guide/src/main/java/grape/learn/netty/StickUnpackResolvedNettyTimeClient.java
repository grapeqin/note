package grape.learn.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
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
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 利用LineBasedFrameDecoder来解决 TCP 粘包 现象的TimeServer Client实现
 *
 * @author grape
 * @date 2019-06-05
 */
public class StickUnpackResolvedNettyTimeClient {

  public static final String REQ = "QUERY SERVER TIME" + System.getProperty("line.separator");

  public static void main(String[] args) {
    new StickUnpackResolvedNettyTimeClient().startClient(1080);
  }

  /**
   * 启动client
   *
   * @param port
   */
  public void startClient(int port) {

    EventLoopGroup loopGroup = new NioEventLoopGroup();

    Bootstrap bootstrap =
        new Bootstrap()
            .group(loopGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(
                new ChannelInitializer<SocketChannel>() {
                  @Override
                  protected void initChannel(SocketChannel ch) throws Exception {
                    // 注册 LineBasedFrameDecoder 和 StringDecoder 解码器
                    ch.pipeline()
                        .addLast(new LineBasedFrameDecoder(64))
                        .addLast(new StringDecoder())
                        .addLast(new StickUnpackTimeClientHandler());
                  }
                });
    try {

      // 阻塞等到连接成功
      ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(port)).sync();

      // 阻塞等待channel 关闭再退出程序
      channelFuture.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      if (null != loopGroup) {
        loopGroup.shutdownGracefully();
      }
    }
  }

  /** 时间服务器 client端 业务逻辑处理 */
  private static class StickUnpackTimeClientHandler extends ChannelHandlerAdapter {

    private final ByteBuf message;

    private int counter;

    public StickUnpackTimeClientHandler() {
      this.message = Unpooled.buffer();
      this.message.writeBytes(REQ.getBytes());
      this.counter = 0;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      System.out.println(ctx.channel().id().asLongText() + " occur exception");
      cause.printStackTrace();
      ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      for (int i = 0; i < 100; i++) {
        ctx.writeAndFlush(this.message.copy());
      }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      String rsp = (String) msg;
      System.out.println("server response msg : " + rsp + ",counter: " + ++counter);
    }
  }
}
