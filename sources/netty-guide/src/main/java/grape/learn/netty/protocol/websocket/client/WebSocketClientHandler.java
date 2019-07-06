package grape.learn.netty.protocol.websocket.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;

/**
 * WebSocket client 发送请求
 *
 * @author grape
 * @date 2019-06-13
 */
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

  private WebSocketClientHandshaker clientHandshaker;

  private ChannelPromise channelFuture;

  public WebSocketClientHandler(WebSocketClientHandshaker clientHandshaker) {
    super();
    this.clientHandshaker = clientHandshaker;
  }

  public ChannelPromise getChannelFuture() {
    return channelFuture;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (!clientHandshaker.isHandshakeComplete()) {
      clientHandshaker.finishHandshake(ctx.channel(), (FullHttpResponse) msg);
      System.out.println("client has finish handshake");
      channelFuture.setSuccess();
      return;
    }

    if (msg instanceof TextWebSocketFrame) {
      TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
      System.out.println("WebSocket Client received message: " + textFrame.text());
    } else if (msg instanceof PongWebSocketFrame) {
      System.out.println("WebSocket Client received pong");
    } else if (msg instanceof CloseWebSocketFrame) {
      System.out.println("WebSocket Client received closing");
      ctx.close();
    }
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    channelFuture = ctx.newPromise();
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    clientHandshaker.handshake(ctx.channel());
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("WebSocket channel undisconnect!");
  }
}
