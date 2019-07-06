package grape.learn.netty.protocol.custom;

import grape.learn.netty.protocol.custom.Header.Type;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端握手认证请求和响应处理
 *
 * @author grape
 * @date 2019-06-19
 */
public class NettyMessageClientHandSharkeHandler extends SimpleChannelInboundHandler {

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.fireExceptionCaught(cause);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.writeAndFlush(buildHandSharkeMessage());
  }

  /**
   * 构建握手认证请求消息
   *
   * @return
   */
  private NettyMessage buildHandSharkeMessage() {
    NettyMessage msg = new NettyMessage();
    Header header = new Header();
    header.setType(Type.HANDSHARKE_REQ.getValue());
    msg.setHeader(header);
    return msg;
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (null == msg) {
      ctx.fireChannelRead(msg);
      return;
    }
    if (!(msg instanceof NettyMessage)) {
      ctx.fireChannelRead(msg);
      return;
    }
    NettyMessage nettyMessage = (NettyMessage) msg;
    if (null != nettyMessage.getHeader()
        && nettyMessage.getHeader().getType() == Type.HANDSHARK_RSP.getValue()) {
      Byte body = (Byte) nettyMessage.getBody();
      switch (body) {
        case 0:
          System.out.println("client has success finish handshark");
          ctx.fireChannelRead(msg);
          break;
        case -1:
          System.err.println("client has repeat handshark");
          ctx.close();
          break;
        case -2:
          System.err.println("client has not in whitelist ");
          ctx.close();
          break;
        default:
          System.err.println("client received unknown code");
          ctx.close();
      }
    } else {
      ctx.fireChannelRead(msg);
    }
  }
}
