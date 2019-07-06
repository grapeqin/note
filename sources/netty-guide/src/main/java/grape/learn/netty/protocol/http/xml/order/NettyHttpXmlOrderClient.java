package grape.learn.netty.protocol.http.xml.order;

import java.net.InetSocketAddress;

import grape.learn.netty.protocol.http.xml.pojo.Order;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * 基于Http+Xml的订单客户端实现
 *
 * @author grape
 * @date 2019-06-11
 */
public class NettyHttpXmlOrderClient {
  public static void main(String[] args) {
    new NettyHttpXmlOrderClient().start(1080);
  }

  public void start(int port) {
    EventLoopGroup g = new NioEventLoopGroup();
    Bootstrap b = new Bootstrap();
    b.group(g)
        .channel(NioSocketChannel.class)
        .handler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                    // 解码器
                    .addLast(new HttpResponseDecoder())
                    .addLast(new HttpObjectAggregator(65535))
                    .addLast(new HttpXmlResponseDecoder(Order.class, true))
                    // 编码器
                    .addLast(new HttpRequestEncoder())
                    .addLast(new HttpXmlRequestEncoder())
                    // 业务逻辑Handler
                    .addLast(new OrderClientHandler());
              }
            });

    try {
      b.connect(new InetSocketAddress(port)).sync().channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      g.shutdownGracefully();
    }
  }

  private static class OrderClientHandler extends SimpleChannelInboundHandler {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      HttpXmlRequest<Order> httpXmlRequest =
          new HttpXmlRequest<>(null, OrderFacory.createOrder(123456));
      System.out.println("client send:" + httpXmlRequest);
      ctx.writeAndFlush(httpXmlRequest);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
      System.out.println("============================================");
      if (msg instanceof HttpXmlResponse) {
        System.out.println("server response:" + msg);
      }
    }
  }
}
