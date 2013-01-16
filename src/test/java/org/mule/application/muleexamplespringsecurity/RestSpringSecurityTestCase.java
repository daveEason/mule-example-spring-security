/*
 * $Id: HttpFilterFunctionalTestCase.java 20320 2010-11-24 15:03:31Z dfeist $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.application.muleexamplespringsecurity;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.mule.transport.NullPayload;

import java.util.HashMap;
import java.util.Map;

public class RestSpringSecurityTestCase extends FunctionalTestCase
{
    String jsonOrder = new String("{\"orderId\":\"1\"," +
            "\"customer\":{" +
            "\"firstName\": \"David\"," +
            "\"lastName\": \"Eason\"," +
            "\"city\": \"San Francisco\"," +
            "\"state\": \"California\"" +
            "}," +
            "\"items\":[" +
            "{" +
            "\"productId\": \"1\"," +
            "\"name\": \"WIRELESS ROUTER\"," +
            "\"manufacturer\": \"SONY\"," +
            "\"price\": \"3.00\"," +
            "\"quantity\": \"5\"" +
            "}," +
            "{" +
            "\"productId\": \"2\"," +
            "\"name\": \"WIRELESS KEYBOARD\"," +
            "\"manufacturer\": \"LOGITECH\"," +
            "\"price\": \"5.00\"," +
            "\"quantity\": \"3\"" +
            "}" +
            "]" +
            "}");

    MuleMessage response;

    protected String getConfigResources()
    {
        return "mule-config.xml";
    }

    public void  testCreateOrder() throws Exception
    {
        response = doRequest("mule-realm", "localhost", "admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order", "PUT", jsonOrder, true, true);

        assertNotNull(response);
        assertNull(response.getExceptionPayload());
        assertFalse(response.getPayload() instanceof NullPayload);

        validateJSONResponse(response,"status","Success");

        String responseString = response.getPayloadAsString();
        System.out.println("Create (PUT) Order response received: " + responseString);
    }

    public void  testRetrieveOrder() throws Exception
    {
        response = doRequest("mule-realm", "localhost", "anon_user", "anon_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", null, true, true);

        assertNotNull(response);
        assertNull(response.getExceptionPayload());
        assertFalse(response.getPayload() instanceof NullPayload);

        validateJSONResponse(response,"status","Success");

        String responseString = response.getPayloadAsString();
        System.out.println("Retrieve (GET) Order response received: " + responseString);
    }

    public void  testUpdateOrder() throws Exception
    {
        response = doRequest("mule-realm", "localhost", "admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order", "POST", jsonOrder, true, true);

        assertNotNull(response);
        assertNull(response.getExceptionPayload());
        assertFalse(response.getPayload() instanceof NullPayload);

        validateJSONResponse(response,"status","Success");

        String responseString = response.getPayloadAsString();
        System.out.println("Update (POST) Order response received: " + responseString);
    }

    public void  testDeleteOrder() throws Exception
    {
        response = doRequest("mule-realm", "localhost", "admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "DELETE", null, true, true);

        assertNotNull(response);
        assertNull(response.getExceptionPayload());
        assertFalse(response.getPayload() instanceof NullPayload);

        validateJSONResponse(response,"status","Success");

        String responseString = response.getPayloadAsString();
        System.out.println("Delete (DELETE) Order response received: " + responseString);
    }

    private MuleMessage doRequest(String realm,
                           String host,
                           String username,
                           String password,
                           String urlString,
                           String httpVerb,
                           String payload,
                           boolean handshake,
                           boolean preemptive) throws Exception
    {

        String userPass = username + ":" + password;
        String basicAuth = "Basic " + new String(new Base64().encode(userPass.getBytes()));

        Map httpProps = new HashMap();
        httpProps.put("http.method",httpVerb);
        httpProps.put("Content-Type","application/json");
        httpProps.put("Authorization", basicAuth);

        MuleClient muleClient = muleContext.getClient();
        MuleMessage clientResponse = muleClient.send(urlString, payload, httpProps);

        return clientResponse;
    }

    public void validateJSONResponse(MuleMessage responseMessage, String keyName, String expect)
    {
        ObjectMapper mapper = new ObjectMapper();


        Map<String,Object> map = null;

        try {
            map = mapper.readValue((String) responseMessage.getPayloadAsString(), Map.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expect, map.get(keyName));

    }
}
