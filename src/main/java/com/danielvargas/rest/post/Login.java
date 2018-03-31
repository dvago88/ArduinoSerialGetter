package com.danielvargas.rest.post;

import org.apache.http.client.methods.HttpPost;

public class Login extends PostRequestBase {

    @Override
    public String postData(String url, Object object, String token) {
        String keyValueStr = (String) object;
        HttpPost httpPost = createConnectivity(url, "application/x-www-form-urlencoded", token);
        return executeReq(keyValueStr, httpPost);
    }

    public String postData(String url, Object object) {
        return postData(url, object, "");
    }
}
