package com.toro.helper.modle.photo;

import com.toro.helper.modle.BaseResponeData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Create By liujia
 * on 2018/10/23.
 **/
public class PhotoItem implements Serializable {
    private String folder;
    private String name;
    private String path;

    public static PhotoItem newInstance(JSONObject obj) throws JSONException {
        PhotoItem instance = new PhotoItem();
        instance.folder = obj.getString("folder");
        instance.path = obj.getString("path");
        instance.name = obj.getString("name");
        return instance;
    }

    public PhotoItem(){

    }

    public JSONObject json(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("path",path);
            obj.put("folder",folder);
            obj.put("name",name);
        } catch (Exception e){

        }

        return obj;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
