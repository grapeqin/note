package grape.learn.netty.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

/**
 * 自定义MessagePack编码器
 *
 * @author grape
 * @date 2019-06-06
 */
public class MsgPackEncoder extends MessageToByteEncoder<UserInfo> {
  @Override
  protected void encode(ChannelHandlerContext ctx, UserInfo msg, ByteBuf out) throws Exception {
    // 将msg 写入packer
    MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
    packer.packInt(msg.getAge());
    packer.packString(msg.getName());
    packer.close();
    // 将packer 写入 out
    out.writeBytes(packer.toMessageBuffer().array());
  }
}
