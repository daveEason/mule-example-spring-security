package org.mule.application.muleexamplespringsecurity;

import com.sun.jersey.spi.container.ContainerResponse;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.ordermanagement.OrderConfirmation;


/**
 * Created with IntelliJ IDEA.
 * User: davideason
 * Date: 1/16/13
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class JerseyResponseTransfomer extends AbstractMessageTransformer{

        @Override
        public Object transformMessage(MuleMessage message, String outputEncoding)
                throws TransformerException {

            ContainerResponse cr = (ContainerResponse) message.getInvocationProperty("jersey_response");
            OrderConfirmation orderConfirmation = (OrderConfirmation) cr.getResponse().getEntity();
            message.setPayload(orderConfirmation);

            return message;
        }
}
