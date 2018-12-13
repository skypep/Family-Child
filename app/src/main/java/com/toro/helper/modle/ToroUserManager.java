//package com.toro.helper.modle;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import com.toro.helper.app.App;
//import com.toro.helper.utils.StringUtils;
//
//import org.json.JSONObject;
//
///**
// * Create By liujia
// * on 2018/10/22.
// **/
//public class ToroUserManager {
//    private String token;
//    private String rongYunToken;
//    private String phone;
//    private String pwd;
//    private long loginTime;
//    private boolean isQuickLogin;
//
//    private static ToroUserManager instance;
//
//    private SharedPreferences pre;
//
//    public static ToroUserManager getInstance(Context context) {
//        if(instance == null) {
//            instance = new ToroUserManager(context.getApplicationContext());
//        }
//        return instance;
//    }
//
//    private ToroUserManager(Context context) {
//        pre = context.getSharedPreferences(this.getClass().getName(),Context.MODE_PRIVATE);
//    }
//
//    public void login(String password,String phone,String token) {
//        String toroToken = "";
//        String rongYunToken = "";
//        try{
//            JSONObject obj = new JSONObject(token);
//            toroToken = obj.getString("login");
//        }catch (Exception e) {
//
//        }
//        try{
//            JSONObject obj = new JSONObject(token);
//            rongYunToken = obj.getString("message");
//            App.getInstance().RongYunConnect(rongYunToken); // 连接融云
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        setLoginTime(System.currentTimeMillis());
//        setPhone(phone);
//        setToken(toroToken);
//        setRongYunToken(rongYunToken);
//        setPwd(password);
//        if(StringUtils.isEmpty(password)) {
//            setQuickLogin(true);
//        }else {
//            setQuickLogin(false);
//        }
//    }
//
//    public String getToken() {
//        token = pre.getString("token",token);
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//        pre.edit().putString("token",token).apply();
//    }
//
//    public String getPhone() {
//        phone = pre.getString("phone",phone);
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//        pre.edit().putString("phone",phone).apply();
//    }
//
//    public String getPwd() {
//        pwd = pre.getString("pwd",pwd);
//        return pwd;
//    }
//
//    public void setPwd(String pwd) {
//        this.pwd = pwd;
//        pre.edit().putString("pwd",pwd).apply();
//    }
//
//    public long getLoginTime() {
//        loginTime = pre.getLong("loginTime",loginTime);
//        return loginTime;
//    }
//
//    public void setLoginTime(long loginTime) {
//        this.loginTime = loginTime;
//        pre.edit().putLong("loginTime",loginTime).apply();
//    }
//
//    public String getRongYunToken() {
//        rongYunToken = pre.getString("rongYunToken",rongYunToken);
//        return rongYunToken;
//    }
//
//    public void setRongYunToken(String rongYunToken) {
//        this.rongYunToken = rongYunToken;
//        pre.edit().putString("rongYunToken",rongYunToken).apply();
//    }
//
//    public boolean isQuickLogin() {
//        isQuickLogin = pre.getBoolean("isQuickLogin",isQuickLogin);
//        return isQuickLogin;
//    }
//
//    public void setQuickLogin(boolean quickLogin) {
//        isQuickLogin = quickLogin;
//        pre.edit().putBoolean("isQuickLogin",isQuickLogin).apply();
//    }
//
//}
