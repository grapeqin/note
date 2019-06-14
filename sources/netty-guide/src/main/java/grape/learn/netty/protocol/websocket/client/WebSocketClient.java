package grape.learn.netty.protocol.websocket.client;

import java.net.URI;
import java.net.URISyntaxException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

/**
 * WebSocket Client 实现
 *
 * @author grape
 * @date 2019-06-13
 */
public class WebSocketClient {
  public static void main(String[] args) throws URISyntaxException {
    new WebSocketClient().run(new URI("ws://localhost:8080/websocket"));
  }

  /**
   * 启动Client
   *
   * @param uri
   */
  public void run(URI uri) {
    EventLoopGroup g = new NioEventLoopGroup();
    Bootstrap b = new Bootstrap();

    if (!uri.getScheme().equals("ws")) {
      System.out.println("UnKnow schema!");
    }

    WebSocketClientHandler webSocketClientHandler =
        new WebSocketClientHandler(
            WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null, false, null));

    b.group(g)
        .channel(NioSocketChannel.class)
        .handler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                    .addLast(new HttpClientCodec())
                    .addLast(new HttpObjectAggregator(8196))
                    .addLast(webSocketClientHandler);
              }
            });
    try {
      Channel channel = b.connect(uri.getHost(), uri.getPort()).sync().channel();
      webSocketClientHandler.getChannelFuture().sync();

      // send Text
      for (int i = 0; i < 10; i++) {
        channel.writeAndFlush(new TextWebSocketFrame("This is my first websocket message : " + i));
      }

      // send ping
      channel.writeAndFlush(new PingWebSocketFrame());

      // send close
      channel.writeAndFlush(new CloseWebSocketFrame());

      channel.closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      g.shutdownGracefully();
    }
  }
}
