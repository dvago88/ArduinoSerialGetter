package com.danielvargas.rest;

import com.danielvargas.entities.DataEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class GetRequest {

    public DataEntity[] call_me(String url) throws Exception {
//        String url = "http://localhost:8090/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
//        con.setRequestProperty("User-Agent","Mozilla5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL: " + url);
        System.out.println("Response code: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());

        JSONObject ob = new JSONObject(response.toString());
        JSONArray myResponse = ob.getJSONArray("content");
        DataEntity[] dataEntities = new DataEntity[myResponse.length()];
        for (int i = 0; i < dataEntities.length; i++) {
            DataEntity dataEntity = new DataEntity();
            JSONObject jsonObject = new JSONObject(myResponse.getJSONObject(i));

            dataEntity.setId(jsonObject.getLong("id"));
            dataEntity.setCode(jsonObject.getString("code"));
            dataEntity.setSensor1(jsonObject.getInt("sensor1"));
            dataEntity.setSensor2(jsonObject.getInt("sensor2"));
            dataEntity.setSensor3(jsonObject.getInt("sensor3"));
            dataEntity.setSensor4(jsonObject.getInt("sensor4"));
//            TODO: mirar formato para obtener la hora
//            dataEntity.setLocalDateTime(jsonObject.get("localdatetime"));
        }
        return dataEntities;
        /*JSONObject res = myResponse.getJSONObject(0);

        System.out.println("id: " + res.getInt("id"));
        System.out.println("Data: " + res.getString("data"));*/

    }
}
