package com.toro.helper.modle.data;

import com.amap.api.maps2d.model.LatLng;

import org.json.JSONObject;
import java.io.Serializable;

/**
 * Create By liujia
 * on 2018/11/3.
 **/
public class LocationInfo implements Serializable{
    private double lng;
    private double lat;
    private String poiTitle;
    private String time;

    public static LocationInfo newInstance(JSONObject obj) {
        LocationInfo instance = new LocationInfo();
        try{
            instance.lat = obj.getDouble("latitude");
            instance.lng = obj.getDouble("longitude");
            instance.poiTitle = obj.getString("poi");
            instance.time = obj.getString("time");
            return instance;
        }catch (Exception e) {

        }
        return null;
    }

    public String getPoiTitle() {
        return poiTitle;
    }

    public void setPoiTitle(String poiTitle) {
        this.poiTitle = poiTitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLatLng(LatLng latLng) {
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(lat,lng);
    }
}
