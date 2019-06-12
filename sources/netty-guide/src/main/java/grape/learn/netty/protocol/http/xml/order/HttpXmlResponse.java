package grape.learn.netty.protocol.http.xml.order;

import io.netty.handler.codec.http.FullHttpResponse;
import lombok.Getter;
import lombok.ToString;

/**
 * 应用层承载server端响应信息的结构体
 *
 * @author grape
 * @date 2019-06-12
 */
@Getter
@ToString
public class HttpXmlResponse<T> {
  private FullHttpResponse response;

  private T t;

  public HttpXmlResponse(FullHttpResponse response, T t) {
    this.response = response;
    this.t = t;
  }
}
