package grape.learn.netty.protocol.custom;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import grape.learn.netty.protocol.custom.Header.Type;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 服务端握手认证请求和响应处理
 *
 * @author grape
 * @date 2019-06-19
 */
public class NettyMessageServerHandSharkeHandler extends SimpleChannelInboundHandler<NettyMessage> {

  /** 存放认证信息 */
  private Map<String, Boolean> authMap = new ConcurrentHashMap<>();

  private String[] whiteList = new String[] {"172.16.1.111"};

  @Override
  protected void messageReceived(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
    System.err.println("current handsharke handler : " + this);
    if (null != msg.getHeader() && msg.getHeader().getType() == Type.HANDSHARKE_REQ.getValue()) {
      System.out.println(
          "server "
              + ctx.channel().remoteAddress()
              + " received handsharke request message : "
              + msg);
      handleHandSharker(ctx);
    } else {
      ctx.fireChannelRead(msg);
    }
  }

  private void handleHandSharker(ChannelHandlerContext ctx) {
    String loginIp = getChannelLoginIp(ctx);
    if (authMap.containsKey(loginIp)) {
      System.err.println("Host : " + loginIp + " has repeat handshark.");
      ctx.writeAndFlush(buildNettyMessageResponse((byte) -1));
      return;
    }
    boolean isLoginOk = false;
    for (String ip : whiteList) {
      if (ip.equals(loginIp)) {
        isLoginOk = true;
        break;
      }
    }
    if (!isLoginOk) {
      System.err.println("Host : " + loginIp + " has not in whitelist.");
      ctx.writeAndFlush(buildNettyMessageResponse((byte) -2));
      return;
    }

    authMap.put(loginIp, true);
    System.out.println("Host : " + loginIp + " handshark success.");
    ctx.writeAndFlush(buildNettyMessageResponse((byte) 0));
  }

  private NettyMessage buildNettyMessageResponse(Object body) {
    NettyMessage message = new NettyMessage();
    Header header = new Header();
    header.setType(Type.HANDSHARK_RSP.getValue());
    message.setHeader(header);
    if (null != body) {
      message.setBody(body);
    }
    return message;
  }

  private String getChannelLoginIp(ChannelHandlerContext ctx) {
    InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
    String loginIp = address.getAddress().getHostAddress();
    return loginIp;
  }

  private void clearChannelLoginIp(ChannelHandlerContext ctx) {
    String loginIp = getChannelLoginIp(ctx);
    authMap.remove(loginIp);
    System.out.println("server finish clear host:" + loginIp);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    // 发生异常时要清理当前channel的登录信息
    clearChannelLoginIp(ctx);
    ctx.fireExceptionCaught(cause);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    clearChannelLoginIp(ctx);
    super.channelInactive(ctx);
  }
}
