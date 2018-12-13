package com.toro.helper.modle.data;

import com.toro.helper.modle.photo.PhotoItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create By liujia
 * on 2018/10/31.
 **/
public class ToroLoginUserData {

    private int uid;
    private String phone;
    private String nickName;
    private PhotoItem headPhoto;

    public static ToroLoginUserData newInstance(JSONObject obj) throws JSONException {
        ToroLoginUserData instance = new ToroLoginUserData();
        instance.uid = obj.getInt("uid");
        instance.phone = obj.getString("phone");
        instance.nickName = obj.getString("name");
        try{
            instance.headPhoto = PhotoItem.newInstance(new JSONObject(obj.getString("headPhoto")));
        } catch (Exception e) {
            instance.headPhoto = null;
        }
        return instance;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public PhotoItem getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(PhotoItem headPhoto) {
        this.headPhoto = headPhoto;
    }
}
