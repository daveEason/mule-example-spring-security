package org.ordermanagement;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;


/**
 * An order.
 * 
 * @author david.eason@mulesoft.com
 */
@JsonAutoDetect
public class Order {

	private String orderId;

	/** Customer associated with order */
	private OrderPerson customer;

	/** List of items on an order */
	private List<OrderItem> items;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public OrderPerson getCustomer() {
		return customer;
	}

	public void setCustomer(OrderPerson customer) {
		this.customer = customer;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
}