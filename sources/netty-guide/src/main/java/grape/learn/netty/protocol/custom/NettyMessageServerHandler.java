package grape.learn.netty.protocol.custom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 自定义消息Server端业务处理handler
 *
 * @author grape
 * @date 2019-06-17
 */
public class NettyMessageServerHandler extends SimpleChannelInboundHandler<NettyMessage> {

  @Override
  protected void messageReceived(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
    // TODO 业务处理
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
  }
}
