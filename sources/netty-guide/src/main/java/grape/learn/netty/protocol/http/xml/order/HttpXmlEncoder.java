package grape.learn.netty.protocol.http.xml.order;

import java.net.InetAddress;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

/**
 * 对HttpXmlRequest 编码为 FullHttpRequest 对象
 *
 * @author grape
 * @date 2019-06-11
 */
public class HttpXmlEncoder extends AbstractHttpXmlEncoder<HttpXmlRequest> {

  @Override
  protected void encode(ChannelHandlerContext ctx, HttpXmlRequest msg, List<Object> out)
      throws Exception {
    ByteBuf body = encode0(ctx, msg.getT());
    FullHttpRequest request = msg.getHttpRequest();
    if (null == request) {
      request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/order", body);

      HttpHeaders headers = request.headers();
      headers.set(Names.HOST, InetAddress.getLocalHost().getAddress());
      headers.set(Names.CONNECTION, Values.CLOSE);
      headers.set(Names.ACCEPT_ENCODING, Values.GZIP.toString() + "," + Values.DEFLATE.toString());
      headers.set(Names.ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
      headers.set(Names.ACCEPT_LANGUAGE, "zh");
      headers.set(Names.USER_AGENT, "Netty xml Http Client side");
      headers.set(Names.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    }
    HttpHeaders.setContentLength(request, body.readableBytes());
    out.add(request);
  }
}
