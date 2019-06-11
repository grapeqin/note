package grape.learn.netty.protocol.http.xml.order;

import grape.learn.netty.protocol.http.xml.pojo.Address;
import grape.learn.netty.protocol.http.xml.pojo.Customer;
import grape.learn.netty.protocol.http.xml.pojo.Order;
import grape.learn.netty.protocol.http.xml.pojo.Shipping;

/**
 * 简易OrderFactory，模拟数据进行测试
 *
 * @author grape
 * @date 2019-06-11
 */
public class OrderFacory {
  /**
   * 构造测试Order对象
   *
   * @param orderNumber
   * @return
   */
  public static Order createOrder(long orderNumber) {
    Order order = new Order();
    order.setOrderNumber(orderNumber);

    Address address = new Address();
    address.setCity("深圳");
    address.setCountry("中国");
    address.setPostCode("123321");
    address.setState("广东省");
    address.setStreet1("福田区");
    order.setBillTo(address);

    Customer customer = new Customer();
    customer.setCustomerNumber(orderNumber);
    customer.setFirstName("grape");
    customer.setLastName("qin");
    order.setCustomer(customer);

    order.setShipping(Shipping.INTERNATIONAL_MAIL);
    order.setShipTo(address);
    return order;
  }
}
