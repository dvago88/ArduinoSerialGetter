package com.danielvargas.rest.get;

import com.danielvargas.entities.DataEntity;
import org.json.JSONObject;

import java.util.Map;

public class GetDataEntity extends GetRequestBase {


    public DataEntity makeRequest(String url, String token) throws Exception {
        Map<String, String> res = call_me(url, token);
        if (res.get("responseCode").equals("401")) {
            return null;
        }
        String response = res.get("response");
/*  JSONObject ob = new JSONObject(response.toString());
        JSONArray myResponse = ob.getJSONArray("content");
        DataEntity[] dataEntities = new DataEntity[myResponse.length()];
        for (int i = 0; i < dataEntities.length; i++) {*/
        DataEntity dataEntity = new DataEntity();
        JSONObject jsonObject = new JSONObject(response);

        dataEntity.setId(jsonObject.getLong("id"));
        dataEntity.setRfid(jsonObject.getString("rfid"));
        dataEntity.setStationNumber(jsonObject.getInt("stationNumber"));
        dataEntity.setSensor2(jsonObject.getInt("sensor2"));
        dataEntity.setSensor3(jsonObject.getInt("sensor3"));
        dataEntity.setSensor4(jsonObject.getInt("sensor4"));
//            TODO: mirar formato para obtener la hora
//            dataEntity.setLocalDateTime(jsonObject.get("localdatetime"));
//            dataEntities[i] = dataEntity;
//        }
        return dataEntity;
    }
}
