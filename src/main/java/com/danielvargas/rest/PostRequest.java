package com.danielvargas.rest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


//http://www.oodlestechnologies.com/blogs/Sending-http-request-%7C-Post-String-data-%28-JSON-%29-%7C-with-authentication
public class PostRequest {

    private int responseCode;


    public void postData(String url, Object object) {
        JSONObject jsonObject = new JSONObject(object);
        String jsonData = jsonObject.toString();
        HttpPost httpPost = createConnectivity(url);
        executeReq(jsonData, httpPost);
    }


    private HttpPost createConnectivity(String restUrl) {
        HttpPost post = new HttpPost(restUrl);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        post.setHeader("X-Stream", "true");
        return post;
    }

    private void executeReq(String jsonData, HttpPost httpPost) {
        try {
            executeHttpRequest(jsonData, httpPost);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error while encoding api url : " + e);
        } catch (IOException e) {
            System.out.println("ioException occured while sending http request : " + e);
        } catch (Exception e) {
            System.out.println("exception occured while sending http request : " + e);
        } finally {
            httpPost.releaseConnection();
        }
    }

    private void executeHttpRequest(String jsonData, HttpPost httpPost) throws UnsupportedEncodingException, IOException {
        HttpResponse response;
        String line = "";
        StringBuffer result = new StringBuffer();
        httpPost.setEntity(new StringEntity(jsonData));
        HttpClient client = HttpClientBuilder.create().build();
        response = client.execute(httpPost);
        responseCode = response.getStatusLine().getStatusCode();
        System.out.println("Post parameters : " + jsonData);
        System.out.println("Response Code : " + getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
    }

    public int getResponseCode() {
        return responseCode;
    }
}