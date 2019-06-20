package grape.learn.netty.protocol.custom;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import grape.learn.netty.protocol.custom.Header.Type;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 心跳检测client端处理
 *
 * @author grape
 * @date 2019-06-19
 */
public class NettyMessageClientHeartbeatHandler extends SimpleChannelInboundHandler<NettyMessage> {

  private ScheduledFuture scheduledFuture;

  @Override
  protected void messageReceived(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
    // 如果服务器端握手登录成功，则发送心跳请求
    if (null != msg && null != msg.getHeader()) {
      if (msg.getHeader().getType() == Type.HANDSHARK_RSP.getValue()) {
        scheduledFuture =
            ctx.executor()
                .scheduleAtFixedRate(
                    new Runnable() {
                      @Override
                      public void run() {
                        NettyMessage nettyMessage = new NettyMessage();
                        Header header = new Header();
                        header.setType(Type.HEARTBEAT_REQ.getValue());
                        nettyMessage.setHeader(header);
                        System.out.println(
                            "client send heartbeat message : -----> " + nettyMessage);
                        ctx.writeAndFlush(nettyMessage);
                      }
                    },
                    0,
                    5,
                    TimeUnit.SECONDS);
      } else if (msg.getHeader().getType() == Type.HEARTBEAT_RSP.getValue()) {
        System.out.println("client receive heartbeat message : ------> " + msg);
      } else {
        ctx.fireChannelRead(msg);
      }
    } else {
      ctx.fireChannelRead(msg);
    }
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    if (null != scheduledFuture) {
      scheduledFuture.cancel(true);
    }
    ctx.fireChannelInactive();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    if (null != scheduledFuture) {
      scheduledFuture.cancel(true);
    }
    ctx.fireExceptionCaught(cause);
  }
}
