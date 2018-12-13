package com.toro.helper.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.os.EnvironmentCompat;

import com.toro.helper.activity.MainActivity;
import com.toro.helper.app.ToroRequestCode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Create By liujia
 * on 2018/10/25.
 **/
public class CameraUtils {

    public static final int PHOTO_REQUEST_CODE = ToroRequestCode.CAMERA_CODE + 1;
    public static final int PERMISSION_CAMERA_REQUEST_CODE = ToroRequestCode.CAMERA_CODE + 2;
    public static final int CAMERA_REQUEST_CODE = ToroRequestCode.CAMERA_CODE + 3;

    /**
     * 相机权限
     */
    private static String[] cameraPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    public interface OnCameraPermissionListener {
        public void onHasePermission();
    }

    public static File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.jpg", timeStamp);
        File storageDir = FileUtils.getImageCacheDir();
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageFileName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }



    /**
     * 检查权限并拍照。
     */
    public static void checkPermissionAndCamera(Activity activity,OnCameraPermissionListener listener) {
        if (checkCameraPermission(activity) ){
            //有调起相机拍照。
            listener.onHasePermission();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(activity,
                    cameraPermissions, PERMISSION_CAMERA_REQUEST_CODE);
        }
    }

    private static boolean checkCameraPermission(Activity activity) {
        for(String permission:cameraPermissions) {
            int hasCameraPermission = ContextCompat.checkSelfPermission(activity.getApplication(),
                    permission);
            if(hasCameraPermission != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 打开系统设置
     */
    public static void startAppSettings(Context context) {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 权限弹框
     * @param context
     * @param applyLoad
     */
    public static void showExceptionDialog(final Context context, final boolean applyLoad) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(context.getString(helper.phone.toro.com.imageselector.R.string.hint))
                .setMessage(context.getString(helper.phone.toro.com.imageselector.R.string.p_message))
                .setNegativeButton(context.getString(helper.phone.toro.com.imageselector.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton(context.getString(helper.phone.toro.com.imageselector.R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startAppSettings(context);
            }
        }).show();
    }

    /**
     * 调起相机拍照
     */
    public static String openCamera(Activity activity) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = CameraUtils.createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                String mPhotoPath = photoFile.getAbsolutePath();
                //通过FileProvider创建一个content类型的Uri
                Uri photoUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", photoFile);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                activity.startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);

                return mPhotoPath;
            }
        }
        return "";
    }

}
