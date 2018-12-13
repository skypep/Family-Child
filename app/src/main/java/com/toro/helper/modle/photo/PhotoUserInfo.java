package com.toro.helper.modle.photo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create By liujia
 * on 2018/10/23.
 **/
public class PhotoUserInfo {
    private int uid;
    private String phone;
    private String name;
    private PhotoItem headPhoto;

    public static PhotoUserInfo newInstance(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        PhotoUserInfo instance = new PhotoUserInfo();
        instance.uid = obj.getInt("uid");
        instance.name = obj.getString("name");
        instance.phone = obj.getString("phone");
        try{
            instance.headPhoto = PhotoItem.newInstance(new JSONObject(obj.getString("headPhoto")));
        } catch (Exception e) {
            instance.headPhoto = null;
        }

        return instance;
    }

    public PhotoUserInfo(){

    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhotoItem getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(PhotoItem headPhoto) {
        this.headPhoto = headPhoto;
    }

    public int getUid() {
        return uid;
    }
}
