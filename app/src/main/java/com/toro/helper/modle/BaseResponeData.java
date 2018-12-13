package com.toro.helper.modle;

import org.json.JSONObject;

/**
 * Create By liujia
 * on 2018/10/22.
 **/
public class BaseResponeData {
    private boolean status = false;
    private String responseCode = "";
    private String entry = "";
    private String message = "";

    public static BaseResponeData newInstance(String jsonString) {
        BaseResponeData instance = new BaseResponeData();
        try{
            JSONObject obj = new JSONObject(jsonString);
            instance.status = obj.getBoolean("status");
            instance.responseCode = obj.getString("responseCode");
            if(instance.status) {
                instance.entry = obj.getString("entry");
            } else {
                instance.message = obj.getString("message");
            }

        } catch (Exception e){

        }
        return instance;
    }

    private BaseResponeData(){

    }

    public boolean isStatus() {
        return status;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getEntry() {
        return entry;
    }

    public String getMessage() {
        return message;
    }
}
