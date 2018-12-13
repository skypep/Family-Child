package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.utils.StringUtils;
import com.toro.helper.view.MainActionBar;

/**
 * Create By liujia
 * on 2018/10/19.
 **/
public class SmsCodeActivity extends ToroActivity implements View.OnClickListener {

    private static final String TORO_EXTRA_MODE = "extra_mode";
    private static final String TORO_EXTRA_PHONE = "extra_phone";

    private static final String QUICK_LOGIN_MODE = "quick_login_mode";
    private static final String FORGET_PWD_MODE = "forget_pwd_mode";

    private String startMode;

    public static final int UPDATE_SCODE_TIME = 1;

    private boolean isCanGetScode = true;
    private TextView getScodeView;
    private long scodentervalTime = 60 * 1000;
    private long sCodeGetTime;
    private String sCode;

    private MainActionBar actionBar;
    private Button submitBt;
    private EditText phoneEdit,sCodeEdit;

    private String phoneText,sCodeText;

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
        setContentView(R.layout.sms_code_activity);
        startMode = getIntent().getStringExtra(TORO_EXTRA_MODE);
        phoneText = getIntent().getStringExtra(TORO_EXTRA_PHONE);
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
        if(startMode.equals(QUICK_LOGIN_MODE)) {
            title = getString(R.string.login_quick_login);
            submitBt.setText(getString(R.string.login_submit));
        } else if(startMode.equals(FORGET_PWD_MODE)) {
            title = getString(R.string.forget_pwd);
            submitBt.setText(getString(R.string.next_step));
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
        if(!StringUtils.isEmpty(phoneText)){
            phoneEdit.setText(phoneText);
        }
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

            if(startMode.equals(QUICK_LOGIN_MODE)) {
                ConnectManager.getInstance().getScodeForLogin(this,phoneNum);
            } else if(startMode.equals(FORGET_PWD_MODE)) {
                ConnectManager.getInstance().getScodeForResetPwd(this,phoneText);
            }
        } else {
            return;

        }
    }

    private void submit() {
        if(startMode.equals(QUICK_LOGIN_MODE)) {
            ConnectManager.getInstance().quickLogin(this,phoneText,sCodeText);
        } else if(startMode.equals(FORGET_PWD_MODE)) {
            if(sCodeText.equals(sCode)) {
                startActivity(ResetPassword.newIntent(this,phoneText,sCodeText));
            }else{
                Toast.makeText(this,getString(R.string.scode_error),Toast.LENGTH_LONG);
            }

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
            return status;
        } else {
            String result = (String) object;
            BaseResponeData data = DataModleParser.parserBaseResponeData(result);
            switch (tag) {
                case ConnectManager.GET_SCODE_FOR_LOGIN:
                case ConnectManager.GET_SCODE_FOR_RESET_PWD:
                    sCode = data.getEntry();
                    break;
                case ConnectManager.QUICK_LOGIN:
                    ToroDataModle.getInstance().getLocalData().login("",phoneText,data.getEntry());
                    ToroDataModle.getInstance().updateToroLoginUserData();
                    startActivity(MainActivity.newIntent(this));
                    finish();
                    break;
            }
            return true;
        }
    }

    private void checkSubmitEnable() {
        if(!StringUtils.isEmpty(phoneText) && !StringUtils.isEmpty(sCodeText)) {
            submitBt.setEnabled(true);
        } else {
            submitBt.setEnabled(false);
        }
    }

    public static Intent newQuickLoginIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,SmsCodeActivity.class);
        intent.putExtra(TORO_EXTRA_MODE,QUICK_LOGIN_MODE);
        return intent;
    }

    public static Intent newQuickLoginIntent(Context context,String phone) {
        Intent intent = newQuickLoginIntent(context);
        intent.putExtra(TORO_EXTRA_PHONE,phone);
        return intent;
    }

    public static Intent newForgetPwdIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,SmsCodeActivity.class);
        intent.putExtra(TORO_EXTRA_MODE,FORGET_PWD_MODE);
        return intent;
    }

    public static Intent newForgetPwdIntent(Context context,String phone) {
        Intent intent = newForgetPwdIntent(context);
        intent.putExtra(TORO_EXTRA_PHONE,phone);
        return intent;
    }
}
