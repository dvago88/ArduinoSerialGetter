package com.danielvargas.rest.post;


import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

//http://www.oodlestechnologies.com/blogs/Sending-http-request-%7C-Post-String-data-%28-JSON-%29-%7C-with-authentication
public class PostRequest extends PostRequestBase {

    @Override
    HttpPost createConnectivity(String restUrl, String contentType, String token) {
        HttpPost post = new HttpPost(restUrl);
        post.setHeader("Content-Type", contentType);
        post.setHeader("Accept", "application/json");
        post.setHeader("X-Stream", "true");
        post.setHeader("Authentication", "Bearer " + token);
        post.setHeader("Authorization", "raspberry");
        return post;
    }
}