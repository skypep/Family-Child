package com.toro.helper.modle.data;

import com.toro.helper.modle.BaseResponeData;

import org.json.JSONObject;

/**
 * Create By liujia
 * on 2018/11/5.
 * {
 "gmtCreated": "2018-11-05 13:53:37",
 "gmtUpdated": "2018-11-05 13:54:14",
 "id": 3,
 "uid": 104,
 "today": "2018-11-03",
 "step": 31231,
 "stepDate": "2018-11-05 13:42:11",
 "hrrest": "112",
 "bloodOxygen": "121",
 "measureDate": "2018-11-05 13:42:11"
 }
 **/
public class HealthInfo {
    private String gmtCreated;
    private String gmtUpdated;
    private int id;
    private int uid;
    private String today;
    private int step;
    private String stepDate;
    private String hrrest;
    private String bloodOxygen;
    private String measureDate;

    public static HealthInfo newInstance(String jsonString) {
        HealthInfo instance = new HealthInfo();
        try{
            JSONObject obj = new JSONObject(jsonString);
            instance.gmtCreated = obj.getString("gmtCreated");
            instance.gmtUpdated = obj.getString("gmtUpdated");
            instance.id = obj.getInt("id");
            instance.uid = obj.getInt("uid");
            instance.today = obj.getString("today");
            instance.step = obj.getInt("step");
            instance.stepDate = obj.getString("stepDate");
            instance.hrrest = obj.getString("hrrest");
            instance.bloodOxygen = obj.getString("bloodOxygen");
            instance.measureDate = obj.getString("measureDate");
            return instance;
        } catch (Exception e){

        }
        return null;
    }

    public String getGmtCreated() {
        return gmtCreated;
    }

    public String getGmtUpdated() {
        return gmtUpdated;
    }

    public int getId() {
        return id;
    }

    public int getUid() {
        return uid;
    }

    public String getToday() {
        return today;
    }

    public int getStep() {
        return step;
    }

    public String getStepDate() {
        return stepDate;
    }

    public String getHrrest() {
        return hrrest;
    }

    public String getBloodOxygen() {
        return bloodOxygen;
    }

    public String getMeasureDate() {
        return measureDate;
    }
}
