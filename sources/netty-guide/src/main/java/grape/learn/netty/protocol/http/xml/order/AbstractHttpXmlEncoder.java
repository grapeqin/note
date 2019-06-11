package grape.learn.netty.protocol.http.xml.order;

import java.io.IOException;
import java.io.StringWriter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * 抽象的HttpXmlEncoder 完成业务层面的Object 到 ByteBuf的转换
 *
 * @author grape
 * @date 2019-06-11
 */
public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {

  private IBindingFactory factory;

  protected ByteBuf encode0(ChannelHandlerContext ctx, Object body)
      throws JiBXException, IOException {
    // 暂不考虑性能问题
    factory = BindingDirectory.getFactory(body.getClass());
    StringWriter writer = new StringWriter();
    IMarshallingContext mctx = factory.createMarshallingContext();
    mctx.setIndent(2);
    mctx.marshalDocument(body, null, null, writer);
    String xml = writer.toString();
    System.out.println("httpXmlEncoder 编码后的xml : " + xml);
    writer.close();
    return Unpooled.copiedBuffer(xml.getBytes());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
  }
}
