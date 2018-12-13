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
import android.widget.Toast;

import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.utils.StringUtils;
import com.toro.helper.view.CustomEditText;
import com.toro.helper.view.MainActionBar;

/**
 * Create By liujia
 * on 2018/10/22.
 **/
public class ResetPassword extends ToroActivity implements View.OnClickListener {

    private static final String EXTRA_SCODE = "extra_scode";
    private static final String EXTRA_PHONE = "extra_phone";
    private String phone,scode,pwdText1,pwdText2;

    private MainActionBar actionBar;
    private CustomEditText pwdEdit1,pwdEdit2;
    private boolean showPwd1,showPwd2;
    private Button submitBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_reset_activity);
        actionBar = findViewById(R.id.main_action_view);
        actionBar.updateView(getString(R.string.setting_pwd),R.mipmap.action_back_icon, 0,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        },null);
        pwdEdit1 = findViewById(R.id.pwd_edit1);
        pwdEdit2 = findViewById(R.id.pwd_edit2);
        pwdEdit1.setDrawableRightListener(new CustomEditText.DrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                if(showPwd1) {
                    pwdEdit1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.pwd_trans_hide);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    pwdEdit1.setCompoundDrawables(null, null, rightDrawable, null);
                } else {
                    pwdEdit1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.pwd_trans_display);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    pwdEdit1.setCompoundDrawables(null, null, rightDrawable, null);
                }
                showPwd1 = !showPwd1;
            }
        });
        pwdEdit2.setDrawableRightListener(new CustomEditText.DrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                if(showPwd2) {
                    pwdEdit2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.pwd_trans_display);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    pwdEdit2.setCompoundDrawables(null, null, rightDrawable, null);
                } else {
                    pwdEdit2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.pwd_trans_hide);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    pwdEdit2.setCompoundDrawables(null, null, rightDrawable, null);
                }
                showPwd2 = !showPwd2;
            }
        });
        pwdEdit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pwdText1 = s.toString();
                checkSubmitEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pwdEdit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pwdText2 = s.toString();
                checkSubmitEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        submitBt = findViewById(R.id.submit_button);
        submitBt.setOnClickListener(this);

        scode = getIntent().getStringExtra(EXTRA_SCODE);
        phone = getIntent().getStringExtra(EXTRA_PHONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                if(pwdText1.equals(pwdText2)) {
                    ConnectManager.getInstance().resetPwd(this,phone,pwdText1,scode);
                } else {
                    Toast.makeText(this,getString(R.string.pwd_confirm_erro),Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    private void checkSubmitEnable() {
        if(!StringUtils.isEmpty(pwdText1) && !StringUtils.isEmpty(pwdText2)) {
            submitBt.setEnabled(true);
        } else {
            submitBt.setEnabled(false);
        }
    }

    public static Intent newIntent(Context context,String phone,String scode) {
        Intent intent = new Intent();
        intent.setClass(context,ResetPassword.class);
        intent.putExtra(EXTRA_SCODE,scode);
        intent.putExtra(EXTRA_PHONE,phone);
        return intent;
    }

    @Override
    public boolean bindData(int tag, Object object) {
        boolean status = super.bindData(tag,object);
        if(!status) {
            return status;
        } else {
            String result = (String) object;
            BaseResponeData data = DataModleParser.parserBaseResponeData(result);
            switch (tag) {
                case ConnectManager.RESET_PWD:
                    startActivity(LoginActivity.newIntent(this));
                    finish();
                    break;
            }
            return true;
        }
    }
}
