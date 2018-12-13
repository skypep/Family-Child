package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.toro.helper.R;
import com.toro.helper.RongyunManager;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.utils.StringUtils;
import com.toro.helper.view.CustomEditText;
import com.toro.helper.view.ToroProgressView;

/**
 * Create By liujia
 * on 2018/10/19.
 **/
public class LoginActivity extends ToroActivity implements View.OnClickListener {

    private CustomEditText pwdEdit;
    private EditText phoneEdit;
    private String phoneText,pwdText;
    private Button submitBt;
    private boolean showPwd = false;

    private ToroProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        progressView = findViewById(R.id.toro_progress);
        progressView.setProgressText(getString(R.string.loging));
        pwdEdit = findViewById(R.id.login_pwd_edit);
        phoneEdit = findViewById(R.id.phone_edit);
        submitBt = findViewById(R.id.submit_bt);
        pwdEdit.setDrawableRightListener(new CustomEditText.DrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                if(showPwd) {
                    pwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.pwd_trans_hide);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    pwdEdit.setCompoundDrawables(null, null, rightDrawable, null);
                } else {
                    pwdEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.pwd_trans_display);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    pwdEdit.setCompoundDrawables(null, null, rightDrawable, null);
                }
                showPwd = !showPwd;
            }
        });

        pwdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pwdText = s.toString();
                checkSubmitEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneText = s.toString();
                checkSubmitEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        findViewById(R.id.login_register).setOnClickListener(this);
        findViewById(R.id.login_quick_login).setOnClickListener(this);
        findViewById(R.id.forget_pwd).setOnClickListener(this);
        submitBt.setOnClickListener(this);

        String toastString = getIntent().getStringExtra("toastString");
        if(StringUtils.isNotEmpty(toastString)) {
            Toast.makeText(this,toastString,Toast.LENGTH_LONG).show();
        }
    }

    private void checkSubmitEnable() {
        if(!StringUtils.isEmpty(phoneText) && !StringUtils.isEmpty(pwdText)) {
            submitBt.setEnabled(true);
        } else {
            submitBt.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register:
                startActivity(RegisterActivity.newRegisterIntent(this));
                break;
            case R.id.login_quick_login:
                if(StringUtils.isEmpty(phoneText)) {
                    startActivity(SmsCodeActivity.newQuickLoginIntent(this));
                } else {
                    startActivity(SmsCodeActivity.newQuickLoginIntent(this,phoneText));
                }
                break;
            case R.id.submit_bt:
                submit();
                break;
            case R.id.forget_pwd:
                if(StringUtils.isEmpty(phoneText)) {
                    startActivity(RegisterActivity.newForgotPwdIntent(this));
                } else {
                    startActivity(RegisterActivity.newForgotPwdIntent(this,phoneText));
                }
                break;
        }
    }

    private void submit() {
        ConnectManager.getInstance().getNumberCaptchar(this);
        progressView.show(this);
    }

    @Override
    public boolean bindData(int tag, Object object) {
        boolean status = super.bindData(tag,object);
        if(!status) {
            progressView.hide(this);
            return status;
        } else {
            String result = (String) object;
            BaseResponeData data = DataModleParser.parserBaseResponeData(result);
            switch (tag) {
                case ConnectManager.GET_NUMBER_CAPTCHAR:
                    String captchar = data.getEntry();
                    ConnectManager.getInstance().login(this,phoneText,StringUtils.md5(StringUtils.md5(pwdText) + captchar),captchar);
                    break;
                case ConnectManager.LOGIN:
                    ToroDataModle.getInstance().getLocalData().login(StringUtils.md5(pwdText),phoneText,data.getEntry());
                    ToroDataModle.getInstance().updateToroLoginUserData();
                    startActivity(MainActivity.newIntent(this));
                    finish();
                    progressView.hide(this);
                    break;

            }
            return true;
        }
    }

    public static Intent newIntent(Context context,String toastString) {
        Intent intent = newIntent(context);
        intent.putExtra("toastString",toastString);
        return intent;
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,LoginActivity.class);
        return intent;
    }
}
