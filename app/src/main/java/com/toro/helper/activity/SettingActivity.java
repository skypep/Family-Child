package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.orhanobut.hawk.Hawk;
import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.view.MainActionBar;

import static com.toro.helper.app.AppConfig.KEY_TONE_PHOTO;
import static com.toro.helper.app.AppConfig.KEY_WIFI_PHOTO;

/**
 * Create By liujia
 * on 2018/11/1.
 **/
public class SettingActivity extends ToroActivity implements View.OnClickListener {

    private MainActionBar mainActionBar;
    private Switch mSwitch1,mSwitch2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        mainActionBar = findViewById(R.id.main_action_view);
        mainActionBar.updateView(getResources().getString(R.string.setting), R.mipmap.action_back_icon, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, null);
        mSwitch1 = findViewById(R.id.right_layout_1).findViewById(R.id.switch_view);
        mSwitch2 = findViewById(R.id.right_layout_2).findViewById(R.id.switch_view);
        mSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Hawk.put(KEY_WIFI_PHOTO,isChecked);
            }
        });
        mSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Hawk.put(KEY_TONE_PHOTO,isChecked);
            }
        });
        mSwitch1.setChecked(Hawk.get(KEY_WIFI_PHOTO,false));
        mSwitch2.setChecked(Hawk.get(KEY_TONE_PHOTO,false));
        findViewById(R.id.login_out).setOnClickListener(this);
        findViewById(R.id.about_layout).setOnClickListener(this);
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,SettingActivity.class);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_out:
                ToroDataModle.getInstance().loginOut();
                Intent intent = LoginActivity.newIntent(this);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.about_layout:
//                Beta.checkUpgrade();
                startActivity(AboutActivity.createIntent(this));
                break;
        }
    }
}
