package grape.learn.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * ByteBuf 测试
 *
 * @author grape
 * @date 2019-06-21
 */
public class ByteBufTest {
  public static void main(String[] args) {
    testReadByteBuf();
  }

  public static void testReadByteBuf() {
    ByteBuf src = Unpooled.buffer();
    src.writeLong(10L);

    System.out.println("src readIndex:" + src.readerIndex());
    System.out.println("src writeIndex:" + src.writerIndex());
    System.out.println("src capacity : " + src.capacity());

    System.out.println("====readBytes(4)====");
    ByteBuf tmp = src.readBytes(4);
    System.out.println("tmp readIndex:" + tmp.readerIndex());
    System.out.println("tmp writeIndex:" + tmp.writerIndex());
    System.out.println("tmp capacity : " + tmp.capacity());

    System.out.println("src readIndex:" + src.readerIndex());
    System.out.println("src writeIndex:" + src.writerIndex());
    System.out.println("src capacity : " + src.capacity());

    ByteBuf dst = Unpooled.buffer();
    dst.writerIndex(255);
    System.out.println("dst readIndex:" + dst.readerIndex());
    System.out.println("dst writeIndex:" + dst.writerIndex());
    System.out.println("dst capacity : " + dst.capacity());
    src.readBytes(dst);
    System.out.println("====readBytes(dst)====");
    System.out.println("dst readIndex:" + dst.readerIndex());
    System.out.println("dst writeIndex:" + dst.writerIndex());
    System.out.println("dst capacity : " + dst.capacity());

    src = Unpooled.buffer();
    src.writeInt(1);

    byte[] bytes = new byte[src.readableBytes()];
    src.readBytes(bytes);
  }
}
