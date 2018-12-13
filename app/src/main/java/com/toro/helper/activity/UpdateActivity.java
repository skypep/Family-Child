package com.toro.helper.activity;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.view.MainActionBar;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static android.os.Environment.getDataDirectory;

/**
 * Create By liujia
 * on 2018/11/1.
 **/
public class UpdateActivity extends ToroActivity implements View.OnClickListener {

    private MainActionBar mainActionBar;
    private TextView mAppSizeTv,mCurVersionTv;
    private long appSizeL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);
        mainActionBar = findViewById(R.id.main_action_view);
        mainActionBar.updateView("当前版本", R.mipmap.action_back_icon, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, null);

        mAppSizeTv = findViewById(R.id.mAppSizeTv);
        mCurVersionTv = findViewById(R.id.mCurVersionTv);
        findViewById(R.id.mUpdateBtn).setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkUsageStats(this)) {
                getAppTotalSize(getDataDirectory());
            }else{
                openUsagePermissionSetting(this);
            }
        }
        appSizeL = appSizeL / 1024 / 1024;
        mAppSizeTv.setText("大小："+appSizeL+"M");
        mCurVersionTv.setText("当前版本: "+getVersionName());

    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,UpdateActivity.class);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mUpdateBtn:
                Beta.checkUpgrade();
                break;
        }
    }

    /**
     * 判断是否有#PACKAGE_USAGE_STATS#的权限
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private boolean checkUsageStats(Activity activity){
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager)activity.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), activity.getPackageName());
        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = activity.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) ==
                    PackageManager.PERMISSION_GRANTED;
        } else {
            granted = mode == AppOpsManager.MODE_ALLOWED;
        }
        return granted;
    }

    private void openUsagePermissionSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void getAppTotalSize(File path){
        try {
            StorageStatsManager storageStatsManager = (StorageStatsManager) this.getSystemService(Context.STORAGE_STATS_SERVICE);
            StorageManager storageManager =  (StorageManager) this.getSystemService(Context.STORAGE_SERVICE);
            UUID uuid = storageManager.getUuidForPath(path);
            int uid = getUid(this, this.getPackageName());
            StorageStats storageStats = storageStatsManager.queryStatsForUid(uuid, uid);
            appSizeL = storageStats.getAppBytes() + storageStats.getCacheBytes() + storageStats.getDataBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据应用包名获取对应uid
     */
    private int getUid(Context context, String pakName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(pakName, PackageManager.GET_META_DATA);
            return ai.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String getVersionName(){
        PackageManager packageManager = this.getPackageManager();
        String code = "";
        try {
            PackageInfo info = packageManager.getPackageInfo(this.getPackageName(),0);
            code = info.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return code;
    }



}
