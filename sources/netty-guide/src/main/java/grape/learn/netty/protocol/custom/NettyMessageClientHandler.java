package grape.learn.netty.protocol.custom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * client 应用逻辑handler
 *
 * @author grape
 * @date 2019-06-18
 */
public class NettyMessageClientHandler extends SimpleChannelInboundHandler<NettyMessage> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
    // TODO
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
