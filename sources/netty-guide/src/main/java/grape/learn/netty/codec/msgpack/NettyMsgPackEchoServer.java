package grape.learn.netty.codec.msgpack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 用MessagePack 这种二进制序列化方式实现EchoServer
 *
 * @author grape
 * @date 2019-06-06
 */
public class NettyMsgPackEchoServer {
  public static void main(String[] args) {
    new NettyMsgPackEchoServer().startServer(1080);
  }

  /**
   * 启动server
   *
   * @param port
   */
  public void startServer(int port) {
    EventLoopGroup g = new NioEventLoopGroup();
    EventLoopGroup c = new NioEventLoopGroup();
    ServerBootstrap b = new ServerBootstrap();
    b.group(g, c)
        .channel(NioServerSocketChannel.class)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                    .addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                    .addLast(new MsgPackDecoder())
                    .addLast(new EchoServerHandler());
              }
            });
    try {
      ChannelFuture cf = b.bind(port).sync();

      cf.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      g.shutdownGracefully();
      c.shutdownGracefully();
    }
  }

  private static class EchoServerHandler extends SimpleChannelInboundHandler {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
      UserInfo userInfo = (UserInfo) msg;
      System.out.println("server receive msg : " + userInfo);
    }
  }
}
