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
        response = doRequest("mule-realm", "localhost", "admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", null, true, true);

        assertNotNull(response);
        assertNull(response.getExceptionPayload());
        assertFalse(response.getPayload() instanceof NullPayload);

        validateJSONResponse(response,"status","Success");

        String responseString = response.getPayloadAsString();
        System.out.println("Retrieve (GET) Order response received: " + responseString);
    }

    public void  testUpdateOrder() throws Exception
    {
        response = doRequest("mule-realm", "localhost", "admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "POST", jsonOrder, true, true);

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

//TODO: Complete Authentication and Authorization tests
//    public void testUnAuthenticatedUser() throws Exception
//    {
//        doRequest("mule-realm", "localhost", "anon_user", "incorrect_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", true, true, 401);
//    }
//
//    public void testAuthenticatedUser() throws Exception
//    {
//        doRequest("mule-realm", "localhost", "anon_user", "anon_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", true, true, 200);
//    }
//
//    public void testUnAuthorizedAccess() throws Exception
//    {
////        doRequest("mule-realm", "localhost", "anon_user", "anon_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "PUT", true, true, 405);
//        doRequest("mule-realm", "localhost", "anon_user", "anon_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", true, true, 200);
//
//    }
//
//    public void testAuthorizedAccess() throws Exception
//    {
////        doRequest("mule-realm", "localhost", "admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order", "PUT", true, true, 200);
//        doRequest("mule-realm", "localhost", "admin_user", "admin_password", "http://localhost:4567/authenticate/ordermgmt/order/1", "GET", true, true, 200);
//
//    }
    
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
/*
//        HttpClient client = new DefaultHttpClient();


//        BufferedReader br = null;
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        client.getParams().setAuthenticationPreemptive(preemptive);
//        client.getState().setCredentials(new AuthScope(host, -1, realm),
//                new UsernamePasswordCredentials(username, password));
//        HttpMethod httpMethod;

        if (httpVerb.equals("DELETE")) {
//            httpMethod = new DeleteMethod(urlString);

        } else if (httpVerb.equals("POST")){
// PostMethod Approach
//            StringRequestEntity stringRequestEntity = new StringRequestEntity(jsonOrder,"application/json","UTF-8");
//            PostMethod postMethod = new PostMethod(urlString);
//TODO: Remove redudant Content-Type header (according to ddossot)
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

// MuleClient approach
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
//            httpMethod = new PutMethod(urlString);

            Map httpProps = new HashMap();
            httpProps.put("http.method","PUT");
            httpProps.put("Content-Type","application/json");
            httpProps.put("Authorization", basicAuth);

            MuleClient muleClient = muleContext.getClient();
            MuleMessage response = muleClient.send(urlString, jsonOrder, httpProps);

            assertNotNull(response);
            assertNull(response.getExceptionPayload());
            assertFalse(response.getPayload() instanceof NullPayload);

            String responseString = response.getPayloadAsString();
            System.out.println("Response received: " + responseString);


        } else {
//            httpMethod = new GetMethod(urlString);
//
//            httpMethod.setDoAuthentication(handshake);

            Map httpProps = new HashMap();
            httpProps.put("http.method","GET");
            httpProps.put("Content-Type","application/json");
            httpProps.put("Authorization", basicAuth);

            MuleClient muleClient = muleContext.getClient();
            MuleMessage response = muleClient.send(urlString, null, httpProps);

            assertNotNull(response);
            assertNull(response.getExceptionPayload());
            assertFalse(response.getPayload() instanceof NullPayload);

            String responseString = response.getPayloadAsString();
            System.out.println("Response received: " + responseString);


//            try
//            {
//                int status = client.executeMethod(httpMethod);
//                assertEquals(result, status);
//            }
//            finally
//            {
//                httpMethod.releaseConnection();
//            }

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
*/


    protected String getUrl()
    {
        return "http://localhost:4567/index.html";
    }

}
