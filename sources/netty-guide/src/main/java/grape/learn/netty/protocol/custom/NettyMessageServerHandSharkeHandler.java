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
    if (null != msg.getHeader() && msg.getHeader().getType() == Type.HANDSHARKE_REQ.getValue()) {
      System.out.println("server received handsharke request message : " + msg);
      handleHandSharker(ctx);
    } else {
      ctx.fireChannelRead(msg);
    }
  }

  private void handleHandSharker(ChannelHandlerContext ctx) {
    String remoteAddress = ctx.channel().remoteAddress().toString();
    if (authMap.containsKey(remoteAddress)) {
      System.err.println("Host : " + remoteAddress + " has repeat handshark.");
      ctx.writeAndFlush(buildNettyMessageResponse((byte) -1));
      return;
    }
    boolean isLoginOk = false;
    InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
    String loginIp = address.getAddress().getHostAddress();
    for (String ip : whiteList) {
      if (ip.equals(loginIp)) {
        isLoginOk = true;
        break;
      }
    }
    if (!isLoginOk) {
      System.err.println("Host : " + remoteAddress + " has not in whitelist.");
      ctx.writeAndFlush(buildNettyMessageResponse((byte) -2));
      return;
    }

    authMap.put(remoteAddress, true);
    System.out.println("Host : " + remoteAddress + " handshark success.");
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
}
