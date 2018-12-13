package com.toro.helper.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;

import com.toro.helper.activity.MainActivity;
import com.toro.helper.utils.okhttp.OkHttp;

import org.json.JSONObject;

/**
 * Create By liujia
 * on 2018/10/24.
 **/
public class HttpUtils {

    public static final boolean useOkHttp = true;

    public static String doPost(String url, JSONObject obj) {
        if(useOkHttp) {
            return OkHttp.doPost(url,obj);
        } else {
            return ToroHttp.doPost(url,obj);
        }

    }

    public static String doTokenPost(String url,JSONObject obj,String token) {
        if(useOkHttp) {
            return OkHttp.doTokenPost(url,obj,token);
        } else {
            return ToroHttp.doPostForToken(url,obj,token);
        }
    }

}
