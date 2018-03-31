package com.danielvargas.rest.post;

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

public abstract class PostRequestBase {
    private int responseCode;


    public String postData(String url, Object object, String token) {
        JSONObject jsonObject = new JSONObject(object);
        String jsonData = jsonObject.toString();
        HttpPost httpPost = createConnectivity(url, "application/json", token);
        return executeReq(jsonData, httpPost);
    }


    HttpPost createConnectivity(String restUrl, String contentType, String token) {
        HttpPost post = new HttpPost(restUrl);
        post.setHeader("Content-Type", contentType);
        post.setHeader("Accept", "application/json");
        post.setHeader("X-Stream", "true");
        return post;
    }

    String executeReq(String jsonData, HttpPost httpPost) {
        try {
            return executeHttpRequest(jsonData, httpPost);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error while encoding api url : " + e);
        } catch (IOException e) {
            System.out.println("ioException occured while sending http request : " + e);
        } catch (Exception e) {
            System.out.println("exception occured while sending http request : " + e);
        } finally {
            httpPost.releaseConnection();
        }
        return "Error, no data return";
    }

    private String executeHttpRequest(String jsonData, HttpPost httpPost) throws UnsupportedEncodingException, IOException {
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
        String responseData = result.toString();
        System.out.println(responseData);
        return responseData;
    }

    public int getResponseCode() {
        return responseCode;
    }

}
