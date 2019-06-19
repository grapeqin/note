package grape.learn.netty.protocol.custom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 私有消息请求头设计
 *
 * @author grape
 * @date 2019-06-17
 */
@Getter
@Setter
@ToString
public final class Header implements Serializable {
  private int crcCode = 0xABEF0101;
  private int length;
  private long sessionID;
  private byte type;
  private byte priority;
  private Map<String, Object> attachment = new HashMap<>();

  public static enum Type {
    BUSINESS_REQ((byte) 0),
    BUSINESS_RSP((byte) 1),
    BUSINESS_ONE_WAY((byte) 2),
    HANDSHARKE_REQ((byte) 3),
    HANDSHARK_RSP((byte) 4),
    HEARTBEAT_REQ((byte) 5),
    HEARTBEAT_RSP((byte) 6);

    private byte value;

    Type(byte value) {
      this.value = value;
    }

    public byte getValue() {
      return value;
    }
  }
}
