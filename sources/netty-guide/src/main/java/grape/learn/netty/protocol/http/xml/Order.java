package grape.learn.netty.protocol.http.xml;

import lombok.ToString;

/**
 * 订单信息
 *
 * @author grape
 * @date 2019-06-10
 */
@ToString
public class Order {
  private long orderNumber;

  private Customer customer;

  private Address billTo;

  private Shipping shipping;

  private Address shipTo;

  private Float total;

  public long getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(long orderNumber) {
    this.orderNumber = orderNumber;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Shipping getShipping() {
    return shipping;
  }

  public void setShipping(Shipping shipping) {
    this.shipping = shipping;
  }

  public Address getShipTo() {
    return shipTo;
  }

  public void setShipTo(Address shipTo) {
    this.shipTo = shipTo;
  }

  public Address getBillTo() {
    return billTo;
  }

  public void setBillTo(Address billTo) {
    this.billTo = billTo;
  }

  public Float getTotal() {
    return total;
  }

  public void setTotal(Float total) {
    this.total = total;
  }
}
