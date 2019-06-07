package grape.learn.netty.codec.msgpack;

import lombok.Data;

/**
 * @author grape
 * @date 2019-06-06
 */
@Data
public class UserInfo {
  private final int age;
  private final String name;
}
