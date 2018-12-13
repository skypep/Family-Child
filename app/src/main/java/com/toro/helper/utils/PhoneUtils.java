package com.toro.helper.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.toro.helper.R;

/**
 * Create By liujia
 * on 2018/11/2.
 **/
public class PhoneUtils {

    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码

    /**
     * 拨打电话（直接拨打）
     * @param telPhone 电话
     */
    public static void call(Activity activity,String telPhone){
        showDialog(activity,telPhone);
    }

    private static void showDialog(final Activity activity,final String phoneNum) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(activity);
        normalDialog.setTitle(R.string.call_dialog_title);
        normalDialog.setMessage(phoneNum);
        normalDialog.setPositiveButton(R.string.call_dialog_yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(checkReadPermission(activity,Manifest.permission.CALL_PHONE,REQUEST_CALL_PERMISSION)){
                            try{
                                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + phoneNum));
                                activity.startActivity(intent);
                            }catch (Exception e) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                Uri data = Uri.parse("tel:" + phoneNum);
                                intent.setData(data);
                                activity.startActivity(intent);

                            }
                        }
                    }
                });
        normalDialog.setNegativeButton(R.string.call_dialog_no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }

    private static boolean checkReadPermission(Activity activity, String string_permission, int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(activity, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(activity, new String[]{string_permission}, request_code);
        }
        return flag;
    }
}
