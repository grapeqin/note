package grape.learn.netty.echo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * ssl单向认证client
 *
 * @author grape
 * @date 2019-07-08
 */
public class OnewaySslClient {

  private SSLSocket socket;

  public OnewaySslClient() throws Exception {
    // 通过套接字工厂，获取一个客户端套接字
    String clientTrustKeyStoreFile = "/Users/qinzy/mynettyclienttrust.keystore";
    String clientTrustKeyPass = "nettyclient";

    KeyStore clientTrustKeyStore = KeyStore.getInstance("JKS");
    clientTrustKeyStore.load(
        new FileInputStream(clientTrustKeyStoreFile), clientTrustKeyPass.toCharArray());

    TrustManagerFactory tmf =
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(clientTrustKeyStore);

    SSLContext sslContext = SSLContext.getInstance("TLSv1");
    sslContext.init(null, tmf.getTrustManagers(), null);
    SSLSocketFactory socketFactory = sslContext.getSocketFactory();
    socket = null;
    socket = (SSLSocket) socketFactory.createSocket("localhost", 7070);
  }

  public void connect() {
    try {
      // 获取客户端套接字输出流
      PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
      // 将用户名和密码通过输出流发送到服务器端
      String userName = "principal";
      output.println(userName);
      String password = "credential";
      output.println(password);
      output.flush();

      // 获取客户端套接字输入流
      BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      // 从输入流中读取服务器端传送的数据内容，并打印出来
      String response = input.readLine();
      response += "\n " + input.readLine();
      System.out.println(response);

      // 关闭流资源和套接字资源
      output.close();
      input.close();
      socket.close();
    } catch (IOException ioException) {
      ioException.printStackTrace();
    } finally {
      System.exit(0);
    }
  }

  public static void main(String args[]) throws Exception {
    System.setProperty("javax.net.debug", "all");
    new OnewaySslClient().connect();
  }
}
