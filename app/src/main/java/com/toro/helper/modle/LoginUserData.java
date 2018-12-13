//package com.toro.helper.modle;
//
///**
// * Create By liujia
// * on 2018/10/29.
// **/
//
//import com.toro.helper.modle.photo.PhotoData;
//import com.toro.helper.modle.photo.PhotoItem;
//import com.toro.helper.modle.photo.PhotoUserInfo;
//import com.toro.helper.modle.photo.PubTime;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * `"entry":
// {"gmtCreated":"2018-10-25 14:15:52",
// "gmtUpdated":"2018-10-25 14:27:09",
// "uid":71,
// "username":"18675532502",
// "password":"",
// "phone":"18675532502",
// "name":"刘佳",
// "nickName":null,
// "gender":null,
// "birthday":null,
// "headPic":null,
// "headPhoto":null,
// "height":null,
// "weight":null,
// "userType":3,
// "isNormal":1,
// "facePersonId":null,
// "fingerPersonId":null,
// "gmtLastLogin":"2018-10-25 14:15:50",
// "captcha":null,
// "captchaValidate":true,
// "serviceVCode":null},
// "e":null}
// */
//public class LoginUserData {
//    private int uid;
//    private String phone;
//    private String nickName;
//    private PhotoItem headPhoto;
//
//    public static LoginUserData newInstance(JSONObject obj) throws JSONException {
//        LoginUserData instance = new LoginUserData();
//        instance.uid = obj.getInt("uid");
//        instance.phone = obj.getString("phone");
//        instance.nickName = obj.getString("nickName");
//        try{
//            instance.headPhoto = PhotoItem.newInstance(new JSONObject(obj.getString("headPhoto")));
//        } catch (Exception e) {
//            instance.headPhoto = null;
//        }
//        return instance;
//    }
//
//    public int getUid() {
//        return uid;
//    }
//
//    public void setUid(int uid) {
//        this.uid = uid;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getNickName() {
//        return nickName;
//    }
//
//    public void setNickName(String nickName) {
//        this.nickName = nickName;
//    }
//
//    public PhotoItem getHeadPhoto() {
//        return headPhoto;
//    }
//
//    public void setHeadPhoto(PhotoItem headPhoto) {
//        this.headPhoto = headPhoto;
//    }
//}
