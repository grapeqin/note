package grape.learn.netty;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 利用LineBasedFrameDecoder来解决 TCP 粘包 现象的TimeServer 实现
 *
 * @author grape
 * @date 2019-06-05
 */
public class StickUnpackResolvedNettyTimeServer {

  public static final String REQ = "QUERY SERVER TIME";

  public static void main(String[] args) {
    new StickUnpackResolvedNettyTimeServer().startServer(1080);
  }

  /**
   * 启动时间服务器
   *
   * @param port
   */
  public void startServer(int port) {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    ServerBootstrap bootstrap =
        new ServerBootstrap()
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)
            .childHandler(
                new ChannelInitializer<SocketChannel>() {
                  @Override
                  protected void initChannel(SocketChannel ch) throws Exception {
                    // 注册 LineBasedFrameDecoder 和 StringDecoder 解码器
                    ch.pipeline()
                        .addLast(new LineBasedFrameDecoder(64))
                        .addLast(new StringDecoder())
                        .addLast(new StickUnpackResolvedTimeServerHandler());
                  }
                });

    try {
      // 调用同步阻塞方法直到绑定成功
      ChannelFuture future = bootstrap.bind(port).sync();
      System.out.println("netty time server has started!");
      // 等待服务器端链路关闭后main函数退出
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      // 优雅退出
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  private static class StickUnpackResolvedTimeServerHandler extends SimpleChannelInboundHandler {

    /** 计数器 */
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
      String body = (String) msg;
      System.out.println("client send message : " + body + " ,counter =" + ++counter);
      String rsp = "BAD REQUEST!";
      if (REQ.equalsIgnoreCase(body)) {
        rsp = LocalDateTime.now().toString() + System.getProperty("line.separator");
      }

      ctx.writeAndFlush(Unpooled.copiedBuffer(rsp.getBytes(StandardCharsets.UTF_8)));
    }
  }
}
