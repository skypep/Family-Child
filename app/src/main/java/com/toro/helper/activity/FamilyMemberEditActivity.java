package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.FamilyMemberInfo;
import com.toro.helper.modle.FamilyUserInfo;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.utils.StringUtils;
import com.toro.helper.view.MainActionBar;
import com.toro.helper.view.ToroProgressView;

/**
 * Create By liujia
 * on 2018/10/29.
 **/
public class FamilyMemberEditActivity extends ToroActivity implements View.OnClickListener {

    private static final String EXTRA_USER_INFO = "extra_user_info";
    private static final String EXTRA_PHONE = "extra_phone";
    private static final String EXTRA_REMARK_NAME = "extra_remark_name";

    public static final String EDIT_RESULT = "edit_result";

    private String phoneText,nameText;
    private EditText phontEdit,nameEdit;
    private FamilyMemberInfo userInfo;
    private Button submitBt;
    private MainActionBar actionBar;
    private ToroProgressView progressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_member_edit_activity);
        userInfo = (FamilyMemberInfo) getIntent().getSerializableExtra(EXTRA_USER_INFO);
        phoneText = getIntent().getStringExtra(EXTRA_PHONE);
        nameText = getIntent().getStringExtra(EXTRA_REMARK_NAME);
        initView();
    }

    private void initView(){
        phontEdit = findViewById(R.id.edit_phone);
        nameEdit = findViewById(R.id.edit_name);
        submitBt = findViewById(R.id.submit_button);
        progressView = findViewById(R.id.toro_progress);
        actionBar = findViewById(R.id.main_action_view);

        phontEdit.addTextChangedListener(new TextWatcher() {
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
        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameText = s.toString();
                checkSubmitEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        String title;
        if(userInfo != null) {
            phontEdit.setText(userInfo.getUserInfo().getPhone());
            nameEdit.setText(userInfo.getRemarkName());
            title = getString(R.string.edit_member);
        }else {
            phontEdit.setText(phoneText);
            nameEdit.setText(nameText);
            title = getString(R.string.create_new_member);
        }
        submitBt.setOnClickListener(this);

        progressView.setProgressText(getString(R.string.submiting));
        actionBar.updateView(title,R.mipmap.action_back_icon, 0,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        },null);
    }

    private void checkSubmitEnable() {
        if(!StringUtils.isEmpty(phoneText) && !StringUtils.isEmpty(nameText)) {
            submitBt.setEnabled(true);
        } else {
            submitBt.setEnabled(false);
        }
    }

    private void submit() {
        progressView.show(this);
        if(userInfo == null) {
            ConnectManager.getInstance().addFamilyMember(this,phoneText,nameText, ToroDataModle.getInstance().getLocalData().getToken());
        } else {
            ConnectManager.getInstance().fixRemarkName(this,userInfo.getId(),nameText,ToroDataModle.getInstance().getLocalData().getToken());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button:
                submit();
                break;
        }
    }

    @Override
    public boolean bindData(int tag, Object object) {
        progressView.hide(this);
        boolean status = super.bindData(tag,object);
        if(status) {
            switch (tag) {
                case ConnectManager.ADD_FAMILY_MENBER:
                    Toast.makeText(this,getString(R.string.create_new_member_sucsses),Toast.LENGTH_LONG).show();
                    break;
                case ConnectManager.FIX_REMARK_NAME:
                    Toast.makeText(this,getString(R.string.submit_sucsses),Toast.LENGTH_LONG).show();
                    break;
            }
            ToroDataModle.getInstance().updateToroFamilyMemberList();
            Intent intent = new Intent();
            intent.putExtra(EDIT_RESULT, true);
            setResult(RESULT_OK, intent);
            finish();

        } else {
            Toast.makeText(this,getString(R.string.submit_failed),Toast.LENGTH_LONG).show();
        }
        return status;
    }

    public static Intent createAddIntent(Context context, FamilyMemberInfo userInfo) {
        Intent intent = new Intent();
        intent.setClass(context,FamilyMemberEditActivity.class);
        intent.putExtra(EXTRA_USER_INFO,userInfo);
        return intent;
    }

    public static Intent createAddIntent(Context context, String phone,String remarkName) {
        Intent intent = new Intent();
        intent.setClass(context,FamilyMemberEditActivity.class);
        intent.putExtra(EXTRA_PHONE,phone);
        intent.putExtra(EXTRA_REMARK_NAME,remarkName);
        return intent;
    }
}
