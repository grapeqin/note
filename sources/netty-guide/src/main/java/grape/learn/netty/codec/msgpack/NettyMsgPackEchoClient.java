package grape.learn.netty.codec.msgpack;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 用MessagePack 这种二进制序列化方式实现EchoClient
 *
 * @author grape
 * @date 2019-06-06
 */
public class NettyMsgPackEchoClient {
  public static void main(String[] args) {
    new NettyMsgPackEchoClient().startClient(1080);
  }

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
                    .addLast(new LengthFieldPrepender(2))
                    .addLast(new MsgPackEncoder())
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

  private class EchoClientHandler extends ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      List<UserInfo> list = userInfos(10);
      for (UserInfo info : list) {
        ctx.writeAndFlush(info);
      }
    }

    private List<UserInfo> userInfos(int size) {
      assert size > 0;
      List<UserInfo> userInfos = new ArrayList<>();
      UserInfo userInfo = null;
      for (int i = 0; i < size; i++) {
        userInfo = new UserInfo(i + 1, "Grape Qin" + (i + 1));
        userInfos.add(userInfo);
      }
      return userInfos;
    }
  }
}
