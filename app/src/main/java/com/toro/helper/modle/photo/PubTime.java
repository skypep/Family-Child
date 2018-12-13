package com.toro.helper.modle.photo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create By liujia
 * on 2018/10/23.
 **/
public class PubTime {
    private int data;
    private int unit;

    public static PubTime newInstance(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        PubTime instance = new PubTime();
        instance.data = obj.getInt("data");
        instance.unit = obj.getInt("unit");
        return instance;
    }

    public PubTime(){

    }

}
