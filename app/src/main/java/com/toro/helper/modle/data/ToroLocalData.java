package com.toro.helper.modle.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.amap.api.maps2d.model.LatLng;
import com.toro.helper.R;
import com.toro.helper.app.App;
import com.toro.helper.utils.StringUtils;

import org.json.JSONObject;

/**
 * Create By liujia
 * on 2018/10/31.
 **/
public class ToroLocalData {

    private String token;
    private String rongYunToken;
    private String phone;
    private String pwd;
    private long loginTime;
    private boolean isQuickLogin;
    // 联网存储，取消本地保存
//    private LocationInfo homeLocation;
//    private int safeguardRadius;
    private boolean agreenPrivacyPolicy;
    private String privatyPolicyContent;

    private static ToroLocalData instance;

    private SharedPreferences pre;

    public ToroLocalData(Context context) {
        pre = context.getSharedPreferences(this.getClass().getName(),Context.MODE_PRIVATE);
//        homeLocation = new LocationInfo();
    }

    public void login(String password,String phone,String token) {
        String toroToken = "";
        String rongYunToken = "";
        try{
            JSONObject obj = new JSONObject(token);
            toroToken = obj.getString("login");
        }catch (Exception e) {

        }
        try{
            JSONObject obj = new JSONObject(token);
            rongYunToken = obj.getString("message");
            App.getInstance().RongYunConnect(rongYunToken); // 连接融云
        }catch (Exception e) {
            e.printStackTrace();
        }
        setLoginTime(System.currentTimeMillis());
        setPhone(phone);
        setToken(toroToken);
        setRongYunToken(rongYunToken);
        setPwd(password);
        if(StringUtils.isEmpty(password)) {
            setQuickLogin(true);
        }else {
            setQuickLogin(false);
        }
    }

    public String getToken() {
        token = pre.getString("token",token);
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        pre.edit().putString("token",token).apply();
    }

    public String getPhone() {
        phone = pre.getString("phone",phone);
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        pre.edit().putString("phone",phone).apply();
    }

    public String getPwd() {
        pwd = pre.getString("pwd",pwd);
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        pre.edit().putString("pwd",pwd).apply();
    }

    public long getLoginTime() {
        loginTime = pre.getLong("loginTime",loginTime);
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
        pre.edit().putLong("loginTime",loginTime).apply();
    }

    public String getRongYunToken() {
        rongYunToken = pre.getString("rongYunToken",rongYunToken);
        return rongYunToken;
    }

    public void setRongYunToken(String rongYunToken) {
        this.rongYunToken = rongYunToken;
        pre.edit().putString("rongYunToken",rongYunToken).apply();
    }

    public boolean isQuickLogin() {
        isQuickLogin = pre.getBoolean("isQuickLogin",isQuickLogin);
        return isQuickLogin;
    }

    public void setQuickLogin(boolean quickLogin) {
        isQuickLogin = quickLogin;
        pre.edit().putBoolean("isQuickLogin",isQuickLogin).apply();
    }

//    public LocationInfo getHomeLocation() {
//        double lat = pre.getFloat("lat",0);
//        double lng = pre.getFloat("lng",0);
//        String poiTitle = pre.getString("poiTitle","");
//        if(lng * lat == 0) {
//            return null;
//        }
//        homeLocation.setLatLng(new LatLng(lat,lng));
//        homeLocation.setPoiTitle(poiTitle);
//        return homeLocation;
//    }
//
//    public void setHomeLocation(LocationInfo homeLocation) {
//        this.homeLocation = homeLocation;
//        pre.edit().putFloat("lat", (float) homeLocation.getLatLng().latitude).putFloat("lng", (float) homeLocation.getLatLng().longitude).putString("poiTitle",homeLocation.getPoiTitle()).apply();
//    }
//
//    public int getSafeguardRadius() {
//        safeguardRadius = pre.getInt("safeguardRadius",safeguardRadius);
//        return safeguardRadius;
//    }
//
//    public void setSafeguardRadius(int safeguardRadius) {
//        this.safeguardRadius = safeguardRadius;
//        pre.edit().putInt("safeguardRadius",safeguardRadius).apply();
//    }

    public boolean isAgreenPrivacyPolicy() {
        agreenPrivacyPolicy = pre.getBoolean("agreenPrivacyPolicy",agreenPrivacyPolicy);
        return agreenPrivacyPolicy;
    }

    public void setAgreenPrivacyPolicy(boolean agreenPrivacyPolicy) {
        this.agreenPrivacyPolicy = agreenPrivacyPolicy;
        pre.edit().putBoolean("agreenPrivacyPolicy",agreenPrivacyPolicy).apply();
    }

    public String getPrivatyPolicyContent(Context context) {
        privatyPolicyContent = pre.getString("privatyPolicyContent",privatyPolicyContent);
        if(StringUtils.isEmpty(privatyPolicyContent)) {
            privatyPolicyContent = context.getString(R.string.privacy_policy);
        }
        return privatyPolicyContent;
    }

    public void setPrivatyPolicyContent(String privatyPolicyContent) {
        this.privatyPolicyContent = privatyPolicyContent;
    }
}
