package grape.learn.netty.protocol.http.xml.order;

import java.io.StringReader;
import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * 将FullHttpRequest body内容 解码为 Object对象
 *
 * @author grape
 * @date 2019-06-11
 */
public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {

  private IBindingFactory factory;

  private Class<?> clazz;

  private boolean isPrint;

  protected AbstractHttpXmlDecoder(Class clazz, boolean isPrint) {
    if (null == clazz) {
      throw new IllegalArgumentException("clazz must not be null");
    }
    this.clazz = clazz;
    this.isPrint = isPrint;
  }

  protected AbstractHttpXmlDecoder(Class clazz) {
    this(clazz, false);
  }

  protected Object decoder0(ChannelHandlerContext ctx, ByteBuf buf) throws JiBXException {
    factory = BindingDirectory.getFactory(this.clazz);
    String xml = buf.toString(Charset.forName("UTF-8"));
    if (this.isPrint) {
      System.out.println("httpRequestDecoder 收到的请求体为： " + xml);
    }
    StringReader reader = new StringReader(xml);
    IUnmarshallingContext uctx = factory.createUnmarshallingContext();
    Object obj = uctx.unmarshalDocument(reader);
    reader.close();
    return obj;
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
    cause.printStackTrace();
  }
}
