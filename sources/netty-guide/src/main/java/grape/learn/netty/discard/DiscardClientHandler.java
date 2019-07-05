package grape.learn.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscardClientHandler extends SimpleChannelInboundHandler<Object> {

  private static final Logger logger = LoggerFactory.getLogger(DiscardClient.class);

  private final int messageSize;
  private ByteBuf content;
  private ChannelHandlerContext ctx;

  public DiscardClientHandler(int messageSize) {
    if (messageSize <= 0) {
      throw new IllegalArgumentException("messageSize: " + messageSize);
    }
    this.messageSize = messageSize;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    logger.info(String.format("channel %s has actived", ctx.channel().id().asLongText()));
    this.ctx = ctx;

    content = ctx.alloc().directBuffer(messageSize).writeZero(messageSize);

    generateTraffic();
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    content.release();
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
    // Server is supposed to send nothing, but if it sends something, discard it.
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    // Close the connection when an exception is raised.
    logger.warn("Unexpected exception from downstream.", cause);
    ctx.close();
  }

  long counter;

  private void generateTraffic() {
    // Flush the outbound buffer to the socket.
    // Once flushed, generate the same amount of traffic again.
    ByteBuf msg = content.duplicate().retain();
    logger.info(String.format("client send message : %s ", msg.toString()));
    ctx.writeAndFlush(msg).addListener(trafficGenerator);
  }

  private final ChannelFutureListener trafficGenerator =
      new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
          if (future.isSuccess()) {
            generateTraffic();
          }
        }
      };
}
