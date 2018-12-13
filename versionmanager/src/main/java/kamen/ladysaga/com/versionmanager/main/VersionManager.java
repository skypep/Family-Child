package kamen.ladysaga.com.versionmanager.main;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

import kamen.ladysaga.com.versionmanager.core.AllenChecker;
import kamen.ladysaga.com.versionmanager.core.VersionParams;
import kamen.ladysaga.com.versionmanager.utils.HttpUtils;

/**
 * Created by SK on 2018/7/3.
 */

public class VersionManager {

    private static VersionManager instance;
    private VersionInfo versionInfo;
    private static String versionUrl = "https://raw.githubusercontent.com/skypep/WorkSpace/master/Doc/update_config";

    public static VersionManager getInstance(){
        if(instance == null ) {
            instance = new VersionManager();
        }
        return instance;
    }

    public void checkVersion(final Activity activity, final Handler handler, final int versionCode){
        new Thread(new Runnable() {
            @Override
            public void run() {
                versionInfo = parserVersionInfo(HttpUtils.getHtml(versionUrl));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if(VersionManager.getInstance().getVersionInfo().getVersionCode() > versionCode) {
                                VersionParams.Builder builder = new VersionParams.Builder()
                                        .setRequestUrl("http://www.baidu.com")
                                        .setService(VersionService.class);
                                if(VersionManager.getInstance().getVersionInfo().isMust()) {
                                    CustomVersionDialogActivity.isForceUpdate = true;
                                    builder.setCustomDownloadActivityClass(CustomVersionDialogActivity.class);
                                }
                                AllenChecker.startVersionCheck(activity, builder.build());
                            }
                        }catch (Exception e) {

                        }
                    }
                });

            }
        }).start();
    }

    public VersionInfo getVersionInfo() {
        return versionInfo;
    }

    private VersionInfo parserVersionInfo(String content) {
        try{
            JSONObject obj = new JSONObject(content);
            VersionInfo info = new VersionInfo();
            info.setDesc(obj.getString("desc"));
            info.setDownloadUrl(obj.getString("downloadUrl"));
            info.setMust(obj.getBoolean("isMust"));
            info.setVersionCode(obj.getInt("versionCode"));
            return info;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
