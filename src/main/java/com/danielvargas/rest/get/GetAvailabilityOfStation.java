package com.danielvargas.rest.get;

import org.json.JSONObject;

import java.util.Map;

public class GetAvailabilityOfStation extends GetRequestBase {

    public int makeRequest(String url) throws Exception {
        Map<String, String> res = call_me(url);
        if (!res.get("responseCode").equals("200")) {
            return -1;
        }
        String response = res.get("response");
        JSONObject jsonObject = new JSONObject(response);
        boolean isAvailable = jsonObject.getBoolean("available");
        if (isAvailable) {
            return 1;
        }
        return 0;
    }
}
