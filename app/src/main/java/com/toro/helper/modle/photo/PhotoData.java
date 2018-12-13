package com.toro.helper.modle.photo;

import com.toro.helper.modle.DataModleParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Create By liujia
 * on 2018/10/23.
 **/
public class PhotoData {
    private int id;
    private String message;
    private int mode;
    private String gmtCreated;
    private String gmtUpdated;
    private PubTime pubTime;
    private PhotoUserInfo userinfo;
    private List<PhotoItem> photos;
    private List<String> remindNames;

    public static PhotoData newInstance(JSONObject obj) throws JSONException {
        PhotoData instance = new PhotoData();
        instance.id = obj.getInt("id");
        instance.message = obj.getString("message");
        instance.mode = obj.getInt("mode");
        instance.gmtCreated = obj.getString("gmtCreated");
        instance.gmtUpdated = obj.getString("gmtUpdated");
        instance.remindNames = DataModleParser.parserRemindNamesItems(obj.getString("remindNames"));
        instance.pubTime = PubTime.newInstance(obj.getString("pubTime"));
        instance.userinfo = PhotoUserInfo.newInstance(obj.getString("user"));
        instance.photos = DataModleParser.parserPhotoItems(obj.getString("photoList"));
        return instance;
    }

    public PhotoData(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(String gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public String getGmtUpdated() {
        return gmtUpdated;
    }

    public void setGmtUpdated(String gmtUpdated) {
        this.gmtUpdated = gmtUpdated;
    }

    public PubTime getPubTime() {
        return pubTime;
    }

    public void setPubTime(PubTime pubTime) {
        this.pubTime = pubTime;
    }

    public PhotoUserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(PhotoUserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public List<PhotoItem> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoItem> photos) {
        this.photos = photos;
    }

    public List<String> getRemindNames() {
        return remindNames;
    }

    public void setRemindNames(List<String> remindNames) {
        this.remindNames = remindNames;
    }

}
