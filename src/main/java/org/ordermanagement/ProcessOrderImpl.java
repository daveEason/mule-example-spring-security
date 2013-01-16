package org.ordermanagement;

import org.springframework.security.access.annotation.Secured;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * REST Web service implementation.
 *
 * @author david.eason@mulesoft.com
 */

@Path("/ordermgmt")
public class ProcessOrderImpl implements IProcessOrder {

    public Order order;
    public String response;
    private OrderConfirmation orderConfirmation = new OrderConfirmation();
    private static Map<String, Order> map = new HashMap<String, Order>();

    /*
      * (non-Javadoc)
      *
      * @see org.ordermgmt.IProcessOrder#retrieveOrder(String orderId)
      */

    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    @Override
    @GET
    @Produces("application/json")
    @Path("/order/{orderId}")
    public OrderConfirmation retrieveOrder(@PathParam("orderId") String orderId) {

        orderId = extractLastPathParam(orderId);

        System.out.println("GET orderId = " + orderId);

        if (ProcessOrderImpl.map.get(orderId) != null) {

            orderConfirmation.setOrder(ProcessOrderImpl.map.get(orderId));
            orderConfirmation.setOrderId(orderId);
            orderConfirmation.setStatus("Success");

        } else {

            orderConfirmation.setOrderId(orderId);
            orderConfirmation.setStatus("Failed - no matching order found for orderId = " + orderId);
        }

        return orderConfirmation;
    }

    /*
      * (non-Javadoc)
      *
      * @see org.ordermgmt.IProcessOrder#createOrder()
      */

    @Secured("ROLE_ADMIN")
    @Override
    @PUT
    @Produces("application/json")
    @Path("/order")
    public OrderConfirmation createOrder(Order order) {

        try {

            int i = ProcessOrderImpl.map.size() + 1;

            String orderId = Integer.toString(i);
            ProcessOrderImpl.map.put(orderId, order);
            orderConfirmation.setOrder(ProcessOrderImpl.map.get(orderId));
            orderConfirmation.setOrderId(orderId);
            orderConfirmation.setStatus("Success");

            System.out.println("PUT orderId = " + orderId);

        } catch (Exception e) {
            orderConfirmation.setStatus("An exception was caught while creating your order");
        }

        System.out.println("PUT orderConfirmation.orderId" + orderConfirmation.getOrderId());
        return orderConfirmation;
    }

    /*
      * (non-Javadoc)
      *
      * @see org.ordermgmt.IProcessOrder#updateOrder(org.ordermgmt.Order)
      */

    @Secured("ROLE_ADMIN")
    @Override
    @POST
    @Produces("application/json")
    @Path("/order")
    public OrderConfirmation updateOrder(Order order) {

        System.out.println("POST orderId = " + order.getOrderId());

        if (ProcessOrderImpl.map.get(order.getOrderId()) != null) {

            ProcessOrderImpl.map.put(order.getOrderId(), order);
            orderConfirmation.setOrder(ProcessOrderImpl.map.get(order.getOrderId()));
            orderConfirmation.setOrderId(order.getOrderId());
            orderConfirmation.setStatus("Success");

        } else {

            orderConfirmation.setOrderId(order.getOrderId());
            orderConfirmation.setStatus("Failed - no matching order found for orderId = " + order.getOrderId());
        }

        return orderConfirmation;
    }

    /*
      * (non-Javadoc)
      *
      * @see org.ordermgmt.IProcessOrder#deleteOrder(String OrderId)
      */

    @Secured("ROLE_ADMIN")
    @Override
    @DELETE
    @Produces("application/json")
    @Path("/order/{orderId}")
    public OrderConfirmation deleteOrder(@PathParam("orderId") String orderId) {

        orderId = extractLastPathParam(orderId);

        System.out.println("DELETE orderId = " + orderId);

        if (ProcessOrderImpl.map.get(orderId) != null) {

            ProcessOrderImpl.map.remove(orderId);
            orderConfirmation.setOrder(ProcessOrderImpl.map.get(orderId));
            orderConfirmation.setOrderId(orderId);
            orderConfirmation.setStatus("Success");

        } else {

            orderConfirmation.setOrderId(orderId);
            orderConfirmation.setStatus("Failed - no matching order found for orderId = " + orderId);
        }

        return orderConfirmation;
    }

    // Hack to get around @Secured annotation messing with @PathParam parameter
    // Currently when I add @Secured annotation it invalidates @PathParam annotations and just passes entire URI into parameter field.
    private String extractLastPathParam(String inParam){

        String firstChar = inParam.substring(0,1);
        String lastParam = inParam;

        if (firstChar.equals("/")) {
            StringTokenizer stringTokenizer = new StringTokenizer(inParam, "/");

            while (stringTokenizer.hasMoreTokens()) {
                lastParam = stringTokenizer.nextToken();
            }
        }

        return lastParam;
    }
}