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

import com.danielvargas.entities.DataEntity;
//http://www.oodlestechnologies.com/blogs/Sending-http-request-%7C-Post-String-data-%28-JSON-%29-%7C-with-authentication
public class PostRequest {


    public void postData(String url, DataEntity dataEntity){

        JSONObject jsonObject = new JSONObject(dataEntity);
        String jsonData = jsonObject.toString();
        PostRequest postRequest = new PostRequest();
        HttpPost httpPost = postRequest.createConnectivity(url);
        postRequest.executeReq(jsonData, httpPost);
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
        System.out.println("Post parameters : " + jsonData);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
    }

/*    public static void main(String[] args) {
        HttpPostReq httpPostReq=new HttpPostReq();
        httpPostReq.postData("http://localhost:8090/arduino/data/v1",new DataEntity(2, "post is working", LocalDate.of(2018, 1, 1)));
    }*/
}