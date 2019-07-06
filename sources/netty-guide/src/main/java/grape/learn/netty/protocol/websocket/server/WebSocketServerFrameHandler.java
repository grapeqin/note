package grape.learn.netty.protocol.websocket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * WebSocket FrameData 处理
 *
 * @author grape
 * @date 2019-06-13
 */
public class WebSocketServerFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {

    if (msg instanceof CloseWebSocketFrame) {
      // 关闭WebSocket通道的数据包
      ctx.writeAndFlush(msg.retain());
      return;
    }
    if (msg instanceof PingWebSocketFrame) {
      // ping Frame
      ctx.writeAndFlush(new PongWebSocketFrame());
      return;
    }

    if (!(msg instanceof TextWebSocketFrame)) {
      ctx.writeAndFlush(new TextWebSocketFrame("Netty server 不支持的WebSocketFrame."));
      return;
    }

    // 业务数据Frame
    String text = ((TextWebSocketFrame) msg).text();
    System.out.println("WebSocket server received : " + text);
    ctx.writeAndFlush(new TextWebSocketFrame("欢迎使用Netty:" + text));
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
  }
}
