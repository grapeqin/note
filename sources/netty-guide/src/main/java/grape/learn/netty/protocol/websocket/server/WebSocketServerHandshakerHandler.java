package grape.learn.netty.protocol.websocket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

/**
 * WebSocket server实现
 *
 * @author grape
 * @date 2019-06-13
 */
public class WebSocketServerHandshakerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  @Override
  protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
    // 1.解码失败直接返回
    if (!req.getDecoderResult().isSuccess()) {
      sendErrorResponse(ctx, HttpResponseStatus.BAD_REQUEST);
      return;
    }

    // 2.请求方法校验
    if (!req.getMethod().equals(HttpMethod.GET)) {
      sendErrorResponse(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
      return;
    }

    // 3.处理握手信息
    WebSocketServerHandshakerFactory handshakerFactory =
        new WebSocketServerHandshakerFactory(getWebSocketUrl(req), null, false);
    WebSocketServerHandshaker handshaker = handshakerFactory.newHandshaker(req);
    if (null == handshaker) {
      WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
    } else {
      handshaker.handshake(ctx.channel(), req);
    }
  }

  private void sendErrorResponse(ChannelHandlerContext ctx, HttpResponseStatus status) {
    ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status));
  }

  /**
   * 获取WebSocket连接信息
   *
   * @param request
   * @return
   */
  private String getWebSocketUrl(FullHttpRequest request) {
    StringBuilder builder = new StringBuilder("ws://");
    builder.append(request.headers().get(HttpHeaders.Names.HOST)).append(request.getUri());
    return builder.toString();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
