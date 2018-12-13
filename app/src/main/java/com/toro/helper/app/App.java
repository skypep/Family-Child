package com.toro.helper.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.toro.helper.BuildConfig;
import com.toro.helper.R;
import com.toro.helper.RongYunListener;
import com.toro.helper.RongyunManager;
import com.toro.helper.activity.LoginActivity;
import com.toro.helper.activity.MainActivity;
import com.toro.helper.modle.data.ToroDataModle;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Create By liujia
 * on 2018/10/26.
 **/
public class App extends Application {

    private static final String Tag = "App";

    public static App instance;
    private Handler handler = new Handler() {
        @Override
        public void publish(LogRecord record) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RongyunManager.getInstance().init(this);
        ToroDataModle.getInstance().init(this);
        Hawk.init(this).build();
        initBugly();
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public void RongYunDisConnect() {
        RongyunManager.getInstance().disConnect();
    }

    public void RongYunConnect(String token) {
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            RongyunManager.getInstance().connect(token,null);
        }
        RongyunManager.getInstance().addConnectionStatusListener(new RongYunListener.ConnectionStatusListener() {
            @Override
            public void onChanged(String name) {
                if(name.equals("KICKED_OFFLINE_BY_OTHER_CLIENT")){
                    // 在别处登陆 强制退出账号
                    ToroDataModle.getInstance().loginOut();
                    Intent intent = LoginActivity.newIntent(instance,getString(R.string.coerce_sigin_out));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }
        });
    }

    private void initBugly(){

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);
        /**
         * true表示初始化时自动检查升级
         * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         */
        Beta.autoCheckUpgrade = false;//设置自动检查
        //如果你不想在通知栏显示下载进度，你可以将这个接口设置为false，默认值为true。  为什么置为false.是因为8.0系统,通知栏要适配
        Beta.enableNotification = false;
        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;60s之内不会重复
         */
        Beta.showInterruptedStrategy = true;
        Bugly.init(this, "0f10e474c0", !BuildConfig.DEBUG);
    }

}
