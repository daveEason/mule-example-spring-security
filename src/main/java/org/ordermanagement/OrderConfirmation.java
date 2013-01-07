package org.ordermanagement;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class OrderConfirmation {

	public String orderId;
	public String status;
	public Order order;
	
	public String getOrderId(){
		return this.orderId;
	}
	
	public void setOrderId(String orderId){
		this.orderId = orderId;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public Order getOrder(){
		return this.order;
	}
	
	public void setOrder(Order order){
		this.order = order;
	}
}
