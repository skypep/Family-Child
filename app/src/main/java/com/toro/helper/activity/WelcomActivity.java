package com.toro.helper.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.utils.StringUtils;

import kamen.ladysaga.com.versionmanager.main.VersionManager;

public class WelcomActivity extends ToroActivity {

    Handler handler = new Handler();

    private String token;
    private String phone;
    private String password;
    private boolean isQuickLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_activity);
        token = ToroDataModle.getInstance().getLocalData().getToken();
        phone = ToroDataModle.getInstance().getLocalData().getPhone();
        password = ToroDataModle.getInstance().getLocalData().getPwd();
        isQuickLogin = ToroDataModle.getInstance().getLocalData().isQuickLogin();
        /**
         * 如果没有登陆过，手动登陆
         * 如果正常登陆过，自动登陆
         * 如果快捷登陆过，检查token是否有效
         * 如果自动登陆失败，手动登陆
         * 自动登陆成功，获取登陆用户信息
         */
//        if(!ToroDataModle.getInstance().getLocalData().isAgreenPrivacyPolicy()) {
////            ConnectManager.getInstance().updatePrivacyPolicy(this);
//            startActivity(PrivacyPolicyActivity.createIntent(this));
//            finish();
//        }else
        if(StringUtils.isEmpty(token)) {
            delayToLoginActivity();
        } else if(isQuickLogin) {
            ConnectManager.getInstance().verifyTokenAction(this,token);
        } else if(StringUtils.isNotEmpty(password)) {
            ConnectManager.getInstance().getNumberCaptchar(this);
        } else {
            delayToLoginActivity();
        }

    }

    private void delayToLoginActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isFinishing()) {
                    return;
                }
                startActivity(LoginActivity.newIntent(WelcomActivity.this));
                finish();
            }
        },2000);
    }

    @Override
    public boolean bindData(int tag, Object object) {
        boolean staus = super.bindData(tag, object);
        BaseResponeData data = DataModleParser.parserBaseResponeData((String) object);
        if(tag == ConnectManager.GET_PRIVACY_POLICY) {
            finish();
            if(staus) {
                try{
                    String content = DataModleParser.parserPrivacyPolicyContent(data.getEntry());
                    if(StringUtils.isNotEmpty(content)) {
                        ToroDataModle.getInstance().getLocalData().setPrivatyPolicyContent(content);
                    }
                }catch (Exception  e) {

                }
            }
            startActivity(PrivacyPolicyActivity.createIntent(this));
        }
        if(staus) {
            switch (tag){
                case ConnectManager.VERIFY_TOKEN:
                    ConnectManager.getInstance().refreshToken(this,token);
                    break;
                case ConnectManager.GET_NUMBER_CAPTCHAR:
                    String captchar = data.getEntry();
                    ConnectManager.getInstance().login(this,phone,StringUtils.md5(password + captchar),captchar);
                    break;
                case ConnectManager.LOGIN:
                    Log.e("****","token=LOGIN==>"+data.getEntry());
                    ToroDataModle.getInstance().getLocalData().login(password,phone,data.getEntry());
                    ToroDataModle.getInstance().updateToroLoginUserData();
                    startActivity(MainActivity.newIntent(WelcomActivity.this));
                    break;
                case ConnectManager.REFRESH_TOKEN:
                    Log.e("****","token=REFRESH_TOKEN==>"+data.getEntry());
                    ToroDataModle.getInstance().getLocalData().login("",phone,data.getEntry());
                    ToroDataModle.getInstance().updateToroLoginUserData();
                    startActivity(MainActivity.newIntent(WelcomActivity.this));
                    finish();
                    break;
            }
        } else {
            startActivity(LoginActivity.newIntent(WelcomActivity.this));
            finish();
        }
        return staus;
    }
}
