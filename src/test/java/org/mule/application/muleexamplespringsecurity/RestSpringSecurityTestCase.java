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

public class RestSpringSecurityTestCase extends FunctionalTestCase {

    private final String jsonOrder;
    private MuleMessage response;

    public RestSpringSecurityTestCase() {
        jsonOrder = "{\"orderId\":\"1\"," +
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
                "}";
    }

    protected String getConfigResources() {
        return "mule-config.xml";
    }

    public void testCreateOrder() throws Exception {
        response = doRequest("admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order", "PUT", jsonOrder);

        assertNotNull(response);
        assertNull(response.getExceptionPayload());
        assertFalse(response.getPayload() instanceof NullPayload);

        validateJSONResponse(response, "status", "Success");

        String responseString = response.getPayloadAsString();
        System.out.println("Create (PUT) Order response received: " + responseString);
    }

    public void testRetrieveOrder() throws Exception {
        response = doRequest("anon_user", "anon_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", null);

        assertNotNull(response);
        assertNull(response.getExceptionPayload());
        assertFalse(response.getPayload() instanceof NullPayload);

        validateJSONResponse(response, "status", "Success");

        String responseString = response.getPayloadAsString();
        System.out.println("Retrieve (GET) Order response received: " + responseString);
    }

    public void testUpdateOrder() throws Exception {
        response = doRequest("admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order", "POST", jsonOrder);

        assertNotNull(response);
        assertNull(response.getExceptionPayload());
        assertFalse(response.getPayload() instanceof NullPayload);

        validateJSONResponse(response, "status", "Success");

        String responseString = response.getPayloadAsString();
        System.out.println("Update (POST) Order response received: " + responseString);
    }

    public void testDeleteOrder() throws Exception {
        response = doRequest("admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "DELETE", null);

        assertNotNull(response);
        assertNull(response.getExceptionPayload());
        assertFalse(response.getPayload() instanceof NullPayload);

        validateJSONResponse(response, "status", "Success");

        String responseString = response.getPayloadAsString();
        System.out.println("Delete (DELETE) Order response received: " + responseString);
    }

    public void testExceptionUnAuthorizedAccess() throws Exception {
        response = doRequest("anon_user", "anon_password", "http://localhost:4567/authenticate/ordermgmt/order", "PUT", jsonOrder);

        assertNotNull(response);
        assertNotNull(response.getExceptionPayload());
        assertFalse(response.getPayload() instanceof NullPayload);

        assertEquals("405", response.getInboundProperty("http.status"));
    }

    public void testExceptionBadCredentials() throws Exception {
        response = doRequest("admin_user", "bad_password", "http://localhost:4567/authenticate/ordermgmt/order", "GET", jsonOrder);

        assertNotNull(response);
        assertNotNull(response.getExceptionPayload());
        assertFalse(response.getPayload() instanceof NullPayload);

        assertEquals("401", response.getInboundProperty("http.status"));

    }

    private MuleMessage doRequest(String username,
                                  String password,
                                  String urlString,
                                  String httpVerb,
                                  String payload) throws Exception {

        String userPass = username + ":" + password;
        String basicAuth = "Basic " + new String(new Base64().encode(userPass.getBytes()));

        Map httpProps = new HashMap();
        try {
            httpProps.put("http.method", httpVerb);
            httpProps.put("Content-Type", "application/json");
            httpProps.put("Authorization", basicAuth);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MuleClient muleClient = muleContext.getClient();

        return muleClient.send(urlString, payload, httpProps);
    }

    public void validateJSONResponse(MuleMessage responseMessage, String keyName, String expect) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> map = mapper.readValue(responseMessage.getPayloadAsString(), Map.class);

            assertNotNull(map.get(keyName));
            assertEquals(expect, map.get(keyName));

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }


    }
}
