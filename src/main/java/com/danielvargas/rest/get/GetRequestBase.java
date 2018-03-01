package com.danielvargas.rest.get;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

abstract class GetRequestBase {

    Map<String, String> call_me(String url) throws Exception {
//        String url = "http://localhost:8090/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
//        con.setRequestProperty("User-Agent","Mozilla5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL: " + url);
        System.out.println("Response code: " + responseCode);
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }catch (IOException ex){
            System.out.println("Si era esta malparida excepcion");
        }
        String inputLine;
        StringBuffer response = new StringBuffer();

        try {
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }catch (NullPointerException ex){
            System.out.println("puto null de mierda");
        }

        String stringRespose = response.toString();
        System.out.println(stringRespose);
        Map<String, String> res = new HashMap<>();
        res.put("response", stringRespose);
        res.put("responseCode", responseCode+"");
        return res;
    }
}
