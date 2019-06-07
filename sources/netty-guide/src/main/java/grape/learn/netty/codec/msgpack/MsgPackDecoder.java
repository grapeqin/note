package grape.learn.netty.codec.msgpack;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

/**
 * 自定义MessagePack解码器
 *
 * @author grape
 * @date 2019-06-06
 */
public class MsgPackDecoder extends ByteToMessageDecoder {
  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    // 从ByteBuf 中读取到数据
    byte[] body = new byte[in.readableBytes()];
    in.readBytes(body);
    // 用MessagePack 反序列化
    MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(body);
    UserInfo userInfo = new UserInfo(unpacker.unpackInt(), unpacker.unpackString());
    unpacker.close();
    // 包装成业务对象
    out.add(userInfo);
  }
}
