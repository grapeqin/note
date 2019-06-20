package grape.learn.netty.protocol.custom;

import grape.learn.netty.protocol.custom.Header.Type;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 心跳消息server端处理
 *
 * @author grape
 * @date 2019-06-19
 */
public class NettyMessageServerHeartbeatHandler extends SimpleChannelInboundHandler<NettyMessage> {
  @Override
  protected void messageReceived(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
    if (null != msg.getHeader() && Type.HEARTBEAT_REQ.getValue() == msg.getHeader().getType()) {
      System.out.println("server receive heartbeat message : <----- :" + msg);
      NettyMessage message = buildHeartBeatResponseMessage();
      System.out.println("server send heartbeat message : <------- : " + message);
      ctx.writeAndFlush(message);
    } else {
      ctx.fireChannelRead(msg);
    }
  }

  private NettyMessage buildHeartBeatResponseMessage() {
    NettyMessage message = new NettyMessage();
    Header header = new Header();
    header.setType(Type.HEARTBEAT_RSP.getValue());
    message.setHeader(header);
    return message;
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.fireExceptionCaught(cause);
  }
}
