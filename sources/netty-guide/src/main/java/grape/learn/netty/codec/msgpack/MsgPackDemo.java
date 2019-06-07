package grape.learn.netty.codec.msgpack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

/**
 * @author grape
 * @date 2019-06-06
 */
public class MsgPackDemo {
  public static void main(String[] args) throws IOException {
    List<String> src = new ArrayList<String>();
    src.add("msgpack");
    src.add("kumofs");
    src.add("viver");

    // 编码
    MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
    packer.packArrayHeader(3);
    for (String s : src) {
      packer.packString(s);
    }
    packer.close();

    // 解码
    MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(packer.toByteArray());
    int size = unpacker.unpackArrayHeader();
    assert 3 == size;
    for (int i = 0; i < size; i++) {
      System.out.println(unpacker.unpackString());
    }
    unpacker.close();

    packer();
  }

  /** packing various types of data */
  public static void packer() throws IOException {
    MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

    // 基本类型
    packer.packBoolean(true);
    packer.packByte((byte) 0x80);
    packer.packShort((short) 1);
    packer.packInt(3);
    packer.packLong(4L);
    packer.packFloat(3.15f);
    packer.packDouble(2.18);
    packer.packNil();
    packer.packString("hello message pack!");

    // packing raw string
    byte[] raw = "utf-8 strings".getBytes(MessagePack.UTF8);
    packer.packRawStringHeader(raw.length);
    packer.writePayload(raw);

    // packing arrays
    int[] arrays = new int[] {3, 5, 1, 0, -1, 255};
    packer.packArrayHeader(arrays.length);
    for (int n : arrays) {
      packer.packInt(n);
    }

    // packing map
    packer.packMapHeader(2);
    packer.packString("apple");
    packer.packInt(1);
    packer.packString("banana");
    packer.packInt(2);

    // packing binary data
    byte[] b = new byte[] {1, 2, 3, 4};
    packer.packBinaryHeader(b.length);
    packer.writePayload(b);

    // packing extend type
    b = "custom data type".getBytes(MessagePack.UTF8);
    packer.packExtensionTypeHeader((byte) 1, b.length);
    packer.addPayload(b);

    packer.close();
  }
}
