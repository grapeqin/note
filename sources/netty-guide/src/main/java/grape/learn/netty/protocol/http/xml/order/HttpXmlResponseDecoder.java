package grape.learn.netty.protocol.http.xml.order;

import java.util.List;

import grape.learn.netty.protocol.http.xml.pojo.Order;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 从FullHttpResponse对象中解码构造HttpXmlResponse对象
 *
 * @author grape
 * @date 2019-06-12
 */
public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<FullHttpResponse> {

  protected HttpXmlResponseDecoder(Class clazz, boolean isPrint) {
    super(clazz, isPrint);
  }

  protected HttpXmlResponseDecoder(Class clazz) {
    super(clazz);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, FullHttpResponse msg, List<Object> out)
      throws Exception {
    if (!msg.getDecoderResult().isSuccess()) {
      System.out.println("receive server response decode failure.");
      return;
    }

    HttpXmlResponse<Order> response = new HttpXmlResponse(msg, decoder0(ctx, msg.content()));
    out.add(response);
  }
}
