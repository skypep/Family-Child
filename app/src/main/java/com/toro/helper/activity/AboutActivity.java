package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.toro.helper.R;
import com.toro.helper.RongyunManager;
import com.toro.helper.app.App;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.view.MainActionBar;

/**
 * Create By liujia
 * on 2018/11/1.
 **/
public class AboutActivity extends ToroActivity implements View.OnClickListener {

    private MainActionBar mainActionBar;
    private TextView mVersionTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        mainActionBar = findViewById(R.id.main_action_view);
        mainActionBar.updateView("关于陪伴助手", R.mipmap.action_back_icon, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, null);

        findViewById(R.id.mIntroductionLayout).setOnClickListener(this);
        findViewById(R.id.mVersionLayout).setOnClickListener(this);
        findViewById(R.id.mFeedbackLayout).setOnClickListener(this);
        findViewById(R.id.mClauseLayout).setOnClickListener(this);
        mVersionTv = findViewById(R.id.version_tv);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mVersionTv.setText("当前版本: "+getVersionName());
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,AboutActivity.class);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mIntroductionLayout:
                Toast.makeText(App.getInstance(),"未建设" + RongyunManager.getInstance().getCurrentConnectionStatus(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.mVersionLayout:
                startActivity(UpdateActivity.createIntent(this));
                break;
            case R.id.mFeedbackLayout:
                startActivity(FeedbackActivity.createIntent(this));
                break;
            case R.id.mClauseLayout:
                startActivity(PrivacyPolicyActivity.createIntent(this));
                break;
        }
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
