package grape.learn.netty;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Netty 实现TimeServer Client端
 *
 * @author grape
 * @date 2019-06-05
 */
public class NettyTimeClient {

  public static final String REQ = "QUERY SERVER TIME";

  public static void main(String[] args) {
    new NettyTimeClient().startClient(1080);
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
                    ch.pipeline().addLast(new IdleStateHandler(15, 22, 33));
                    ch.pipeline().addLast(new TimeClientHandler());
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
  private static class TimeClientHandler extends ChannelHandlerAdapter {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final ByteBuf message;

    public TimeClientHandler() {
      this.message = Unpooled.buffer();
      this.message.writeBytes(REQ.getBytes());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      System.out.println(ctx.channel().id().asLongText() + " occur exception");
      ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      ctx.writeAndFlush(this.message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      ByteBuf buf = (ByteBuf) msg;
      byte[] bytes = new byte[buf.readableBytes()];
      buf.readBytes(bytes);
      String rsp = new String(bytes, StandardCharsets.UTF_8);
      System.out.println("server response msg : " + rsp);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
      if (evt instanceof IdleStateEvent) {
        System.out.println(
            dateTimeFormatter.format(LocalDateTime.now())
                + " idleHandler : "
                + ((IdleStateEvent) evt).state());
      }
      super.userEventTriggered(ctx, evt);
    }
  }
}
