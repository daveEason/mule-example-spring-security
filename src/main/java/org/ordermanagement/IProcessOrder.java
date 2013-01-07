package org.ordermanagement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Interface for order processing.
 * 
 * @author david.eason@mulesoft.com
 */

@Path("/ordermgmt")
public interface IProcessOrder {

		
	/**
	 * Retrieve an Order.
	 * 
	 * @param orderId
	 * @return Order
	 */
    @GET
    @Produces("application/json")
    @Path("/order/{orderId}")
    OrderConfirmation retrieveOrder(String orderId);

	/**
	 * Create an order.
	 * 
	 * @return String orderId
	 */

    OrderConfirmation createOrder(Order order);

	/**
	 * Update an order.
	 * 
	 * @param order
	 * @return Order
	 */
    OrderConfirmation updateOrder(String orderId, Order order);

	/**
	 * Delete an order.
	 * 
	 * @param orderId
	 * @return String
	 */
    OrderConfirmation deleteOrder(String orderId);
}
