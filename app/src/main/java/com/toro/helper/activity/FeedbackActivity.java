package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.toro.helper.R;
import com.toro.helper.app.App;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.view.MainActionBar;
import com.toro.helper.view.ToroProgressView;

/**
 * Create By liujia
 * on 2018/11/1.
 **/
public class FeedbackActivity extends ToroActivity implements View.OnClickListener {

    private MainActionBar mainActionBar;
    private EditText mContentEdt;
    private ToroProgressView progressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        mainActionBar = findViewById(R.id.main_action_view);
        mainActionBar.updateView("关于陪伴助手", R.mipmap.action_back_icon, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, null);

        findViewById(R.id.mSubmitBtn).setOnClickListener(this);
        mContentEdt = findViewById(R.id.mContentEdt);
        progressView = findViewById(R.id.toro_progress);
        progressView.hide(this);
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,FeedbackActivity.class);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mSubmitBtn:
                if (mContentEdt.getText().toString().isEmpty()){
                    Toast.makeText(App.getInstance(),"请输入内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressView.show(this);
                ConnectManager.getInstance().feedback(this,mContentEdt.getText().toString());
                break;
        }
    }

    @Override
    public boolean bindData(int tag, Object object) {
        switch (tag) {
            case ConnectManager.FEEDBACK:
                progressView.hide(this);
                boolean status = super.bindData(tag,object);
                if(!status) {
                    Toast.makeText(this,getString(R.string.submit_failed),Toast.LENGTH_LONG).show();
                    return status;
                } else {
                    Toast.makeText(this,getString(R.string.submit_sucsses),Toast.LENGTH_LONG).show();
                    finish();
                    return true;
                }
        }
        return false;
    }
}
