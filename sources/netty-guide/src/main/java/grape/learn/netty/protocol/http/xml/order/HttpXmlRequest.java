package grape.learn.netty.protocol.http.xml.order;

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Getter;
import lombok.ToString;

/**
 * 承载HttpRequest和业务对象
 *
 * @author grape
 * @date 2019-06-11
 */
@ToString
@Getter
public class HttpXmlRequest<T> {

  private final FullHttpRequest httpRequest;

  private final T t;

  public HttpXmlRequest(FullHttpRequest httpRequest, T t) {
    this.httpRequest = httpRequest;
    this.t = t;
  }
}
