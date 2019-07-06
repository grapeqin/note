package grape.learn.netty.protocol.http.xml.order;

import java.util.ArrayList;
import java.util.List;

import grape.learn.netty.protocol.http.xml.pojo.Customer;
import grape.learn.netty.protocol.http.xml.pojo.Order;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 基于Http+Xml的订单server实现
 *
 * @author grape
 * @date 2019-06-11
 */
public class NettyHttpXmlOrderServer {
  public static void main(String[] args) {
    new NettyHttpXmlOrderServer().start(1080);
  }

  public void start(int port) {
    EventLoopGroup g = new NioEventLoopGroup();
    EventLoopGroup worker = new NioEventLoopGroup();

    ServerBootstrap b = new ServerBootstrap();
    b.group(g, worker)
        .channel(NioServerSocketChannel.class)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                    // 解码器
                    .addLast(new HttpRequestDecoder())
                    .addLast(new HttpObjectAggregator(65535))
                    .addLast(new HttpXmlRequestDecoder(Order.class, true))
                    // 编码器
                    .addLast(new HttpResponseEncoder())
                    .addLast(new HttpXmlResponseEncoder())
                    // 业务逻辑Handler
                    .addLast(new OrderServerHandler());
              }
            });

    try {
      b.bind(port).sync().channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      g.shutdownGracefully();
      worker.shutdownGracefully();
    }
  }

  private static class OrderServerHandler extends SimpleChannelInboundHandler {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      super.exceptionCaught(ctx, cause);
      cause.printStackTrace();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
      HttpXmlRequest<Order> request = (HttpXmlRequest<Order>) msg;
      Order order = request.getT();
      System.out.println("server receiced domain obj : " + order);
      System.out.println("==========================================");
      doHandler(order);

      ctx.writeAndFlush(new HttpXmlResponse<>(null, order));
    }

    public void doHandler(Order order) {
      if (null == order) {
        return;
      }
      Customer customer = order.getCustomer();
      List<String> middleNames = new ArrayList<>();
      middleNames.add("qinpi");
      middleNames.add("laoqin");
      middleNames.add("yansuoer");
      customer.setMiddleNames(middleNames);
    }
  }
}
