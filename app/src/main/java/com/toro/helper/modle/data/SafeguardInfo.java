package com.toro.helper.modle.data;

import com.amap.api.maps2d.model.LatLng;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Create By liujia
 * on 2018/12/3.
 **/
public class SafeguardInfo implements Serializable {
    private LocationInfo locationInfo;
    private long scope;// 范围半径

    public static SafeguardInfo newInstance(String result) {
        SafeguardInfo instance = new SafeguardInfo();
        try{
            JSONObject obj = new JSONObject(result);
            float lng = (float) obj.getDouble("longitude");
            float lat = (float) obj.getDouble("latitude");
            LocationInfo locationInfo = new LocationInfo();
            LatLng latLng = new LatLng(lat,lng);
            locationInfo.setLatLng(latLng);
            locationInfo.setPoiTitle(obj.getString("position"));

            instance.scope = obj.getLong("scope");
            instance.locationInfo = locationInfo;
            return instance;
        }catch (Exception e) {

        }
        return null;
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public long getScope() {
        return scope;
    }

    public void setScope(long scope) {
        this.scope = scope;
    }
}
