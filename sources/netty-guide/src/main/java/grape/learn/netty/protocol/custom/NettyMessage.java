package grape.learn.netty.protocol.custom;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 私有消息结构
 *
 * @author grape
 * @date 2019-06-17
 */
@Setter
@Getter
@ToString
public final class NettyMessage<T> implements Serializable {
  // 消息头
  private Header header;
  // 消息体
  private T body;
}
