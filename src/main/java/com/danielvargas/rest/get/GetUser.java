package com.danielvargas.rest.get;

import com.danielvargas.entities.DataEntity;
import org.json.JSONObject;

import java.util.Map;

public class GetUser extends GetRequestBase {
    public long makeRequest(String url) throws Exception {
        Map<String, String> res = call_me(url);
        if (res.get("responseCode").equals("401")) {
            return -1;
        }
        String response = res.get("response");
//        TODO: procesar el response
        return 0;
    }
}
