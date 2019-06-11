package grape.learn.netty.protocol.http.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import grape.learn.netty.protocol.http.xml.pojo.Order;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * 演示JiBx的基本使用方法
 *
 * @author grape
 * @date 2019-06-11
 */
public class TestOrder {

  private IBindingFactory factory;

  private static final String CHARSET_NAME = "UTF-8";

  {
    try {
      factory = BindingDirectory.getFactory(Order.class);
    } catch (JiBXException e) {
      e.printStackTrace();
    }
  }

  /**
   * 将Order对象编码成xml字符串
   *
   * @param order
   * @return
   * @throws JiBXException
   * @throws IOException
   */
  private String encode2Xml(Order order) throws JiBXException, IOException {
    StringWriter writer = new StringWriter();
    IMarshallingContext mctx = factory.createMarshallingContext();
    mctx.setIndent(2);
    mctx.marshalDocument(order, CHARSET_NAME, null, writer);
    String xml = writer.toString();
    writer.close();
    System.out.println(xml);
    /*
         *<?xml version="1.0" encoding="UTF-8"?>
    <order xmlns="http://grape.learn/netty/protocol/http/xml" orderNumber="123"/>
         * */
    return xml;
  }

  /**
   * 将xml字符串解码为Order对象
   *
   * @param xml
   * @return
   * @throws JiBXException
   */
  private Order decodeFromXml(String xml) throws JiBXException {
    StringReader reader = new StringReader(xml);
    IUnmarshallingContext uctx = factory.createUnmarshallingContext();
    Order order = (Order) uctx.unmarshalDocument(reader);
    return order;
  }

  public static void main(String[] args) throws JiBXException, IOException {
    TestOrder test = new TestOrder();
    Order order = new Order();
    order.setOrderNumber(123);
    String body = test.encode2Xml(order);
    Order order2 = test.decodeFromXml(body);
    System.out.println(order2);
    /*
     *Order(orderNumber=123, customer=null, billTo=null, shipping=null, shipTo=null, total=null)
     * */
  }
}
