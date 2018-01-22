package com.danielvargas.rest;

import com.danielvargas.entities.DataEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

//http://www.oodlestechnologies.com/blogs/Sending-http-request-%7C-Post-String-data-%28-JSON-%29-%7C-with-authentication
public class PutRequest {


    public void putData(String url, DataEntity dataEntity) {

        JSONObject jsonObject = new JSONObject(dataEntity);
        String jsonData = jsonObject.toString();
        PutRequest putRequest = new PutRequest();
        HttpPut httpPut = putRequest.createConnectivity(url);
        putRequest.executeReq(jsonData, httpPut);
    }


    private HttpPut createConnectivity(String restUrl) {
        HttpPut put = new HttpPut(restUrl);
        put.setHeader("Content-Type", "application/json");
        put.setHeader("Accept", "application/json");
        put.setHeader("X-Stream", "true");
        return put;
    }

    private void executeReq(String jsonData, HttpPut httpPut) {
        try {
            executeHttpRequest(jsonData, httpPut);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error while encoding api url: " + e);
        } catch (IOException e) {
            System.out.println("ioException occured while sending http request : " + e);
        } catch (Exception e) {
            System.out.println("exception occured while sending http request : " + e);
        } finally {
            httpPut.releaseConnection();
        }
    }

    private void executeHttpRequest(String jsonData, HttpPut httpPut) throws UnsupportedEncodingException, IOException {
        HttpResponse response;
        String line = "";
        StringBuffer result = new StringBuffer();
        httpPut.setEntity(new StringEntity(jsonData));
        HttpClient client = HttpClientBuilder.create().build();
        response = client.execute(httpPut);
        System.out.println("Put parameters : " + jsonData);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
    }
}
