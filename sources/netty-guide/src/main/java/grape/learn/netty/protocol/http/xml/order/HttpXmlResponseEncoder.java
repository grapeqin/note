package grape.learn.netty.protocol.http.xml.order;

import java.util.List;

import grape.learn.netty.protocol.http.xml.pojo.Order;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * 将HttpXmlResponse对象编码为 FullHttpResponse对象
 *
 * @author grape
 * @date 2019-06-12
 */
public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse<Order>> {
  @Override
  protected void encode(ChannelHandlerContext ctx, HttpXmlResponse<Order> msg, List<Object> out)
      throws Exception {
    FullHttpResponse response = msg.getResponse();
    ByteBuf body = encode0(ctx, msg.getT());
    if (null == response) {
      response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
    } else {
      response =
          new DefaultFullHttpResponse(
              msg.getResponse().getProtocolVersion(), msg.getResponse().getStatus(), body);
    }
    HttpHeaders.setHeader(response, Names.CONTENT_TYPE, "text/xml");
    HttpHeaders.setContentLength(response, body.readableBytes());
    out.add(response);
  }
}
