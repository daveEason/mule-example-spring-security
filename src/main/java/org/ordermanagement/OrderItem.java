package org.ordermanagement;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * An item in an order.
 * 
 * @author Derek
 */
@JsonAutoDetect
public class OrderItem {

	/** Item number */
	private String productId;

	/** Item name */
	private String name;
	
	/** Item manufacturer */
	private String manufacturer;

	/** Item price */
	private float price;

	/** Item quantity */
	private int quantity;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}