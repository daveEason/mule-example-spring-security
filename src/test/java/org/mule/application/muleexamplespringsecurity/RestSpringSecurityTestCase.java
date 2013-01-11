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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.mule.transport.NullPayload;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestSpringSecurityTestCase extends FunctionalTestCase
{
    String jsonOrder = new String("{\"orderId\":\"967546567\"," +
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

    String simpleJsonOrder = new String("{\"orderId\": \"967546567\"}");

    protected String getConfigResources()
    {
        return "mule-config.xml";
    }

    //TODO: Order tests to create (PUT), retrieve (GET), update (POST) and delete (DELETE) orders in correct sequence
    //TODO: Complete PUT and POST correctly (add message payload)
    public void testUnAuthenticatedUser() throws Exception
    {
        doRequest("mule-realm", "localhost", "anon_user", "incorrect_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", true, true, 401);
    }

    public void testAuthenticatedUser() throws Exception
    {
        doRequest("mule-realm", "localhost", "anon_user", "anon_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", true, true, 200);
    }

    public void testUnAuthorizedAccess() throws Exception
    {
//        doRequest("mule-realm", "localhost", "anon_user", "anon_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "PUT", true, true, 405);
        doRequest("mule-realm", "localhost", "anon_user", "anon_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", true, true, 200);

    }

    public void testAuthorizedAccess() throws Exception
    {
        doRequest("mule-realm", "localhost", "admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", true, true, 200);
        doRequest("mule-realm", "localhost", "admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "POST", true, true, 200);
    }
    
    private void doRequest(String realm,
                           String host,
                           String username,
                           String password,
                           String urlString,
                           String httpVerb,
                           boolean handshake,
                           boolean preemptive, int result) throws Exception
    {
        HttpClient client = new HttpClient();
        BufferedReader br = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        client.getParams().setAuthenticationPreemptive(preemptive);
        client.getState().setCredentials(new AuthScope(host, -1, realm),
                new UsernamePasswordCredentials(username, password));
        HttpMethod httpMethod;

        if (httpVerb.equals("DELETE")) {
            httpMethod = new DeleteMethod(urlString);

        } else if (httpVerb.equals("POST")){
// PostMethod Approach
//            StringRequestEntity stringRequestEntity = new StringRequestEntity(jsonOrder,"application/json","UTF-8");
//            PostMethod postMethod = new PostMethod(urlString);
//            postMethod.setRequestHeader("Content-Type","application/json");
//            postMethod.setRequestEntity(stringRequestEntity);
//            postMethod.setDoAuthentication(handshake);
//
//            int status = client.executeMethod(postMethod);


// HttpURLConnection Approach
//            params.add(new NameValuePair("Content-Type","application/json"));
//            params.add(new NameValuePair("order", order));
//            HttpPost httpPost = new HttpPost(url);
//            httpPost.setEntity(new UrlEncodedFormEntity((List<? extends org.apache.http.NameValuePair>) params));

//            URL url = new URL(urlString);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String userPass = username + ":" + password;
            String basicAuth = "Basic " + new String(new Base64().encode(userPass.getBytes()));
//
//            try {
//                connection.setDoOutput(true);
//                connection.setDoInput(true);
//                connection.setUseCaches(false);
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Content-Type", "application/json");
//                connection.setRequestProperty("Content-Length",Integer.toString(jsonOrder.getBytes().length));
//                connection.setRequestProperty("Authorization", basicAuth);
//
////                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
////                outputStreamWriter.write(jsonOrder);
////                outputStreamWriter.flush();
//
//                //Send (POST) request
//                DataOutputStream wr = new DataOutputStream (
//                        connection.getOutputStream ());
//                wr.writeBytes (jsonOrder);
//                wr.flush ();
//                wr.close ();
//
//                if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//                    throw new RuntimeException("Failed : Http error code : " + connection.getResponseCode());
//                } else {
//                    assertEquals(result, connection.getResponseCode());
//                }
//
//                br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
//
//                String output;
//                System.out.println("Output from Server .... \n");
//                while ((output = br.readLine()) != null) {
//                    System.out.println(output);
//                }
//
//                connection.disconnect();
//
//            } catch (MalformedURLException me) {
//                me.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            Map httpProps = new HashMap();
            httpProps.put("http.method","POST");
            httpProps.put("Content-Type","application/json");
            httpProps.put("Authorization", basicAuth);

            MuleClient muleClient = muleContext.getClient();
            MuleMessage response = muleClient.send(urlString, simpleJsonOrder, httpProps);

            assertNotNull(response);
            assertNull(response.getExceptionPayload());
            assertFalse(response.getPayload() instanceof NullPayload);

            String responseString = response.getPayloadAsString();
            System.out.println("Response received: " + responseString);

//            assertEquals(result, status);

        } else if (httpVerb.equals("PUT")) {
            httpMethod = new PutMethod(urlString);

        } else {
            httpMethod = new GetMethod(urlString);

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

//        httpMethod.setDoAuthentication(handshake);
//
//        try
//        {
//            int status = client.executeMethod(httpMethod);
//
//            if(status == HttpStatus.SC_NOT_IMPLEMENTED) {
//                System.err.println("The requested method is not implemented by this URI: " + httpVerb);
//                httpMethod.getResponseBodyAsString();
//            } else {
//                br = new BufferedReader(new InputStreamReader(httpMethod.getResponseBodyAsStream()));
//                String readLine;
//                while(((readLine = br.readLine()) != null)) {
//                    System.err.println(readLine);
//                }
//            }
//
//            assertEquals(result, status);
//        }
//        finally
//        {
//            httpMethod.releaseConnection();
//            if(br != null) try {br.close(); } catch (Exception fe) {}
//        }
    }

    protected String getUrl()
    {
        return "http://localhost:4567/index.html";
    }

}
