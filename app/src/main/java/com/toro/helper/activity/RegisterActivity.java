package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.utils.StringUtils;
import com.toro.helper.view.CustomEditText;
import com.toro.helper.view.MainActionBar;
import com.toro.helper.view.ToroProgressView;

/**
 * Create By liujia
 * on 2018/10/22.
 **/
public class RegisterActivity  extends ToroActivity implements View.OnClickListener {

    public static final int UPDATE_SCODE_TIME = 1;
    private static final String TORO_EXTRA_MODE = "extra_mode";
    private static final String TORO_EXTRA_PHONE = "extra_phone";

    private static final String REGISTER_MODE = "register_mode";
    private static final String FORGET_PWD_MODE = "forget_pwd_mode";

    private boolean isCanGetScode = true;
    private TextView getScodeView;
    private long scodentervalTime = 60 * 1000;
    private long sCodeGetTime;
    private String sCode;
    private String startMode;

    private MainActionBar actionBar;
    private Button submitBt;
    private EditText phoneEdit,sCodeEdit;
    private CustomEditText pwdEdit1,pwdEdit2;
    private boolean showPwd1,showPwd2;
    private ToroProgressView progressView;

    private String phoneText,sCodeText,pwdText1,pwdText2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_SCODE_TIME:
                    long curTime = System.currentTimeMillis();
                    long passTime = curTime - sCodeGetTime;
                    if(passTime > scodentervalTime){
                        resetScode();
                    } else {
                        String string = getResources().getString(R.string.get_s_code_format_time);
                        String stringFormat = String.format(string, (scodentervalTime - passTime)/1000);
                        getScodeView.setTextColor(getResources().getColor(R.color.gray_text_color));
                        getScodeView.setText(stringFormat);
                        sendEmptyMessageDelayed(UPDATE_SCODE_TIME,500);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        startMode = getIntent().getStringExtra(TORO_EXTRA_MODE);
        progressView = findViewById(R.id.toro_progress);
        getScodeView = findViewById(R.id.get_s_code);
        getScodeView.setOnClickListener(this);
        submitBt = findViewById(R.id.submit_button);
        submitBt.setOnClickListener(this);
        actionBar = findViewById(R.id.main_action_view);

        phoneEdit = findViewById(R.id.edit_phone);
        sCodeEdit = findViewById(R.id.scode_edit);
        initView();
    }

    private void initView() {
        String title = "";
        if(REGISTER_MODE.equals(startMode)) {
            title = getString(R.string.login_register);
            submitBt.setText(getString(R.string.login_register));
            progressView.setProgressText(getString(R.string.registing));
        } else if(FORGET_PWD_MODE.equals(startMode)) {
            title = getString(R.string.forget_pwd);
            submitBt.setText(getString(R.string.submit));
            progressView.setProgressText(getString(R.string.submiting));
        }
        actionBar.updateView(title,R.mipmap.action_back_icon, 0,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        },null);
        sCodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sCodeText = s.toString();
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
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.pwd_trans_hide);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    pwdEdit2.setCompoundDrawables(null, null, rightDrawable, null);
                } else {
                    pwdEdit2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.pwd_trans_display);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_s_code:
                getScode();
                break;
            case R.id.submit_button:
                submit();
                break;
        }
    }

    private void getScode() {
        if(isCanGetScode) {
            String phoneNum = phoneEdit.getText().toString();
            if(StringUtils.isEmpty(phoneNum)) {
                Toast.makeText(this,getString(R.string.please_input_phone),Toast.LENGTH_LONG).show();
                return;
            }
            isCanGetScode = false;
            sCodeGetTime = System.currentTimeMillis();
            handler.sendEmptyMessage(UPDATE_SCODE_TIME);
            if(REGISTER_MODE.equals(startMode)) {
                ConnectManager.getInstance().getScodeForRegister(this,phoneNum);
            } else if(FORGET_PWD_MODE.equals(startMode)) {
                ConnectManager.getInstance().getScodeForResetPwd(this,phoneText);
            }

        } else {
            return;

        }
    }

    private void submit() {
        if(pwdText1.equals(pwdText2)) {
            if(REGISTER_MODE.equals(startMode)) {
                ConnectManager.getInstance().register(this,phoneText,sCodeText,StringUtils.md5(pwdText1));
            } else if(FORGET_PWD_MODE.equals(startMode)) {
                ConnectManager.getInstance().resetPwd(this,phoneText,StringUtils.md5(pwdText1),sCodeText);
            }
            progressView.show(this);
        } else {
            Toast.makeText(this,getString(R.string.pwd_confirm_erro),Toast.LENGTH_LONG).show();
        }

    }

    private void resetScode() {
        getScodeView.setTextColor(getResources().getColor(R.color.blue_text_color));
        if(isCanGetScode){
            getScodeView.setText(getResources().getString(R.string.get_s_code));
        }else {
            getScodeView.setText(getResources().getString(R.string.reget_s_code));
        }
        isCanGetScode = true;
    }


    @Override
    public boolean bindData(int tag, Object object) {
        boolean status = super.bindData(tag,object);
        if(!status) {
            resetScode();
            progressView.hide(this);
            return status;
        } else {
            String result = (String) object;
            BaseResponeData data = DataModleParser.parserBaseResponeData(result);
            switch (tag) {
                case ConnectManager.GET_SCODE_FOR_REGISTER:
                case ConnectManager.GET_SCODE_FOR_RESET_PWD:
                    sCode = data.getEntry();
                    break;
                case ConnectManager.REGISTER:
                    startActivity(LoginActivity.newIntent(this));
                    finish();
                    progressView.hide(this);
                    break;
                case ConnectManager.RESET_PWD:
                    startActivity(LoginActivity.newIntent(this));
                    finish();
                    progressView.hide(this);
                    break;
            }
            return true;
        }

    }

    private void checkSubmitEnable() {
        if(!StringUtils.isEmpty(phoneText) && !StringUtils.isEmpty(sCodeText) && !StringUtils.isEmpty(pwdText1) && !StringUtils.isEmpty(pwdText2)) {
            submitBt.setEnabled(true);
        } else {
            submitBt.setEnabled(false);
        }
    }

    public static Intent newRegisterIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,RegisterActivity.class);
        intent.putExtra(TORO_EXTRA_MODE,REGISTER_MODE);
        return intent;
    }

    public static Intent newForgotPwdIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,RegisterActivity.class);
        intent.putExtra(TORO_EXTRA_MODE,FORGET_PWD_MODE);
        return intent;
    }

    public static Intent newForgotPwdIntent(Context context,String phone) {
        Intent intent = newForgotPwdIntent(context);
        intent.putExtra(TORO_EXTRA_PHONE,phone);
        return intent;
    }

}
