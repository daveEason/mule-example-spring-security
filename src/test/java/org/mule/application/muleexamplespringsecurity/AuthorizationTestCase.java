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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.mule.tck.FunctionalTestCase;

public class AuthorizationTestCase extends FunctionalTestCase
{
    String order = new String("{\"orderId\":\"967546567\"," +
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
    
    protected String getConfigResources()
    {
        return "mule-config.xml";
    }

    //TODO: Order tests to create (PUT), retrieve (GET), update (POST) and delete (DELETE) orders in correct sequence
    //TODO: Figure out how to do PUT and POST correctly (add message payload)
    public void testUnauthroizedUser() throws Exception
    {
//        doRequest("mule-realm", "localhost", "anon_user", "anon_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "PUT", true, true, 405);
        doRequest("mule-realm", "localhost", "anon_user", "anon_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", true, true, 405);

    }

    public void testAuthroizedUser() throws Exception
    {
        doRequest("mule-realm", "localhost", "admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", true, true, 200);
//        doRequest("mule-realm", "localhost", "admin", "admin", "http://localhost:4567/authenticate/ordermgmt/order/1", "POST", true, true, 200);
    }
    
    private void doRequest(String realm,
                           String host,
                           String username,
                           String password,
                           String url,
                           String method,
                           boolean handshake,
                           boolean preemptive, int result) throws Exception
    {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(preemptive);
        client.getState().setCredentials(new AuthScope(host, -1, realm),
                new UsernamePasswordCredentials(username, password));
        HttpMethod httpMethod;

        if (method.equals("DELETE")) {
            httpMethod = new DeleteMethod(url);

        } else if (method.equals("POST")){
            httpMethod = new PostMethod(url);
            httpMethod.getRequestHeader("Content-Type").setValue("application/json");

        } else if (method.equals("PUT")) {
            httpMethod = new PutMethod(url);
            httpMethod.getRequestHeader("Content-Type").setValue("application/json");

        } else {
            httpMethod = new GetMethod(url);
        }

        httpMethod.setDoAuthentication(handshake);

        try
        {
            int status = client.executeMethod(httpMethod);
            assertEquals(result, status);
        }
        finally
        {
            httpMethod.releaseConnection();
        }
    }

    protected String getUrl()
    {
        return "http://localhost:4567/index.html";
    }

}
