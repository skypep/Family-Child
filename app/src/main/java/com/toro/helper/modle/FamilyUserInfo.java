package com.toro.helper.modle;

import com.toro.helper.modle.photo.PhotoItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Create By liujia
 * on 2018/10/23.
 **/
public class FamilyUserInfo implements Serializable {
    private int uid;
    private String phone;
    private String name;
    private PhotoItem headPhoto;

    private int isNormal;
    private String password;
    private int userType;
    private String username;
    private String gmtCreated;
    private String gmtLastLogin;
    private String gmtUpdated;
    private String sn;

    public static FamilyUserInfo newInstance(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        FamilyUserInfo instance = new FamilyUserInfo();
        instance.uid = obj.getInt("uid");
        instance.name = obj.getString("name");
        instance.phone = obj.getString("phone");

        instance.isNormal = obj.getInt("isNormal");
        instance.password = obj.getString("password");
        instance.userType = obj.getInt("userType");
        instance.username = obj.getString("username");
        instance.gmtCreated = obj.getString("gmtCreated");
        instance.gmtLastLogin = obj.getString("gmtLastLogin");
        instance.gmtUpdated = obj.getString("gmtUpdated");
        instance.sn = obj.getString("sn");
        try{
            instance.headPhoto = PhotoItem.newInstance(new JSONObject(obj.getString("headPhoto")));
        } catch (Exception e) {
            instance.headPhoto = null;
        }

        return instance;
    }

    public FamilyUserInfo(){

    }

    public int getUid() {
        return uid;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public PhotoItem getHeadPhoto() {
        return headPhoto;
    }

    public int getIsNormal() {
        return isNormal;
    }

    public String getPassword() {
        return password;
    }

    public int getUserType() {
        return userType;
    }

    public String getUsername() {
        return username;
    }

    public String getGmtCreated() {
        return gmtCreated;
    }

    public String getGmtLastLogin() {
        return gmtLastLogin;
    }

    public String getGmtUpdated() {
        return gmtUpdated;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeadPhoto(PhotoItem headPhoto) {
        this.headPhoto = headPhoto;
    }

    public void setIsNormal(int isNormal) {
        this.isNormal = isNormal;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGmtCreated(String gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public void setGmtLastLogin(String gmtLastLogin) {
        this.gmtLastLogin = gmtLastLogin;
    }

    public void setGmtUpdated(String gmtUpdated) {
        this.gmtUpdated = gmtUpdated;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
