package grape.learn.netty.discard;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {

  private static final Logger logger = LoggerFactory.getLogger(DiscardServerHandler.class);

  @Override
  public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
    logger.debug(String.format("server has received message : %s", msg));
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    logger.warn("Unexpected exception from downstream.", cause);
    ctx.close();
  }
}
