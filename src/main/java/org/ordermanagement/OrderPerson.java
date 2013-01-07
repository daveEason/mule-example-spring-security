package org.ordermanagement;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Person associated with an order.
 * 
 * @author david.eason@mulesoft.com
 */
@JsonAutoDetect
public class OrderPerson {

	/** First name */
	private String firstName;

	/** Last name */
	private String lastName;

	/** City */
	private String city;

	/** State */
	private String state;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}