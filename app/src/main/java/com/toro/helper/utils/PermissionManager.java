package com.toro.helper.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.toro.helper.activity.MainActivity;
import com.toro.helper.app.ToroRequestCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By liujia
 * on 2018/11/23.
 **/
public class PermissionManager {

    private static String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    private static List<String> mPermissionList = new ArrayList<>();

    public static void checkPermission(Activity activity) {
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了

        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(activity, permissions, ToroRequestCode.ALL_PERMISSION_REQUEST_CODE);
        }
    }
}
