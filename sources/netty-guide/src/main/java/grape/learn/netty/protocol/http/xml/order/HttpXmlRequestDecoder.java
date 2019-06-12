package grape.learn.netty.protocol.http.xml.order;

import java.nio.charset.Charset;
import java.util.List;

import grape.learn.netty.protocol.http.xml.pojo.Order;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * 从FullHttpRequest中decode 组装成HttpXmlRequest 对象
 *
 * @author grape
 * @date 2019-06-11
 */
public class HttpXmlRequestDecoder extends AbstractHttpXmlDecoder<FullHttpRequest> {

  public HttpXmlRequestDecoder(Class clazz, boolean isPrint) {
    super(clazz, isPrint);
  }

  public HttpXmlRequestDecoder(Class clazz) {
    super(clazz);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out)
      throws Exception {
    if (!msg.getDecoderResult().isSuccess()) {
      sendError(ctx, HttpResponseStatus.BAD_REQUEST);
      return;
    }
    HttpXmlRequest<Order> httpXmlRequest = new HttpXmlRequest(msg, decoder0(ctx, msg.content()));
    out.add(httpXmlRequest);
  }

  private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
    FullHttpResponse response =
        new DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            status,
            Unpooled.copiedBuffer(
                "Failure：" + status.toString() + "\r\n".toCharArray(), Charset.defaultCharset()));
    response.headers().set(Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
  }
}
