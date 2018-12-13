package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
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
import com.toro.helper.modle.data.ToroLoginUserData;
import com.toro.helper.modle.photo.PhotoItem;
import com.toro.helper.utils.CameraUtils;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.utils.ImageLoad;
import com.toro.helper.utils.PhoneUtils;
import com.toro.helper.utils.StringUtils;
import com.toro.helper.utils.okhttp.mutifile.listener.impl.UIProgressListener;
import com.toro.helper.view.MainActionBar;
import com.toro.helper.view.RoundnessImageView;
import com.toro.helper.view.ToroProgressView;
import com.toro.helper.view.iphone.IphoneDialogBottomMenu;
import com.toro.helper.view.iphone.MenuItemOnClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import helper.phone.toro.com.imageselector.utils.ImageSelector;

/**
 * Create By liujia
 * on 2018/10/30.
 **/
public class EditPersonalDetailsActivity extends ToroActivity implements View.OnClickListener {

    private String nameText = "";
    private EditText phontEdit,nameEdit;
    private Button submitBt;
    private MainActionBar actionBar;
    private ToroProgressView progressView;
    private RoundnessImageView headImageView;
    private ToroLoginUserData loginUserData;
    private TextView editHeadImageView;

    private String mPhotoPath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_personal_details_activity);
        initView();
    }

    private void initView(){
        phontEdit = findViewById(R.id.edit_phone);
        nameEdit = findViewById(R.id.edit_name);
        submitBt = findViewById(R.id.submit_button);
        progressView = findViewById(R.id.toro_progress);
        actionBar = findViewById(R.id.main_action_view);
        headImageView = findViewById(R.id.head_img);
        editHeadImageView = findViewById(R.id.edit_head_img);

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
        loginUserData = ToroDataModle.getInstance().getLoginUserData();
        if(loginUserData != null) {
            if(loginUserData.getHeadPhoto() != null) {
                editHeadImageView.setText(R.string.fix_head);
                ImageLoad.GlidLoad(headImageView,loginUserData.getHeadPhoto(),R.mipmap.default_head);
            } else {
                editHeadImageView.setText(R.string.add_head);
                headImageView.setImageResource(R.mipmap.default_head);
            }
            if(StringUtils.isNotEmpty(loginUserData.getNickName())) {
                nameEdit.setText(loginUserData.getNickName());
            } else {
            }
            phontEdit.setText(loginUserData.getPhone());
        }
        progressView.setProgressText(getString(R.string.submiting));
        actionBar.updateView(getString(R.string.edit_personal_details),R.mipmap.action_back_icon, 0,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        },null);

        editHeadImageView.setOnClickListener(this);
        submitBt.setOnClickListener(this);
    }

    private void checkSubmitEnable() {
        /**
         * 修改了名字或者头像可提交
         */
        if(loginUserData != null) {
            if((!nameText.equals(loginUserData.getNickName()) && StringUtils.isNotEmpty(nameText)) || StringUtils.isNotEmpty(mPhotoPath)) {
                submitBt.setEnabled(true);
            }
        } else {
            submitBt.setEnabled(false);
        }
    }

    private void showEditPhotoAction() {
        ArrayList<String> menus = new ArrayList<>();
        menus.add(getString(R.string.take_photo_by_camera));
        menus.add(getString(R.string.choose_photo_from_album));
        IphoneDialogBottomMenu dialog = new IphoneDialogBottomMenu(EditPersonalDetailsActivity.this,menus,new MenuItemOnClickListener() {
            @Override
            public void onClickMenuItem(View v, int item_index, String item) {
                if(item.equals(getString(R.string.take_photo_by_camera))) {
                    CameraUtils.checkPermissionAndCamera(EditPersonalDetailsActivity.this, new CameraUtils.OnCameraPermissionListener() {
                        @Override
                        public void onHasePermission() {
                            mPhotoPath = CameraUtils.openCamera(EditPersonalDetailsActivity.this);
                        }
                    });
                } else if(item.equals(getString(R.string.choose_photo_from_album))){
                    ImageSelector.builder()
                            .useCamera(true) // 设置是否使用拍照
                            .setSingle(true)  //设置是否单选
                            .setViewImage(true) //是否点击放大图片查看,，默认为true
                            .setCrop(true)      //可裁剪
                            .setMaxSelectCount(1) // 图片的最大选择数量，小于等于0时，不限数量。
                            .start(EditPersonalDetailsActivity.this, CameraUtils.PHOTO_REQUEST_CODE); // 打开相册
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraUtils.PHOTO_REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            mPhotoPath = images.get(0);
            changeHeadPhoto();
        }else if (requestCode == CameraUtils.CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                changeHeadPhoto();
            }
        }
        checkSubmitEnable();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CameraUtils.PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，有调起相机拍照。
                mPhotoPath = CameraUtils.openCamera(this);
            } else {
                //拒绝权限，弹出提示框。
                CameraUtils.showExceptionDialog(this,false);
            }
        }
    }

    private void changeHeadPhoto() {
        if(StringUtils.isNotEmpty(mPhotoPath)) {
            ImageLoad.GlidLoad(headImageView,mPhotoPath,R.mipmap.default_head);
        }
    }
    private void showProgress(int progress) {
        progressView.show(this);
        String formatString = getResources().getString(R.string.upload_head_progress_text_format);
        String text = String.format(formatString, progress + "%");
        progressView.setProgressText(text);
    }


    private void submit() {
        if(StringUtils.isNotEmpty(mPhotoPath)) {
            showProgress(0);
            ConnectManager.getInstance().uploadPhot(this,this, mPhotoPath, ToroDataModle.getInstance().getLocalData().getToken(), new UIProgressListener() {
                @Override
                public void onUIProgress(long currentBytes, long contentLength, boolean done) {
                    showProgress((int) (currentBytes * 100 / contentLength));
                }
            });
        } else {
            progressView.setProgressText(getString(R.string.upload_head_finish_submit_progress_text));
            progressView.show(this);
            ConnectManager.getInstance().submitPersionalDaitels(this,loginUserData.getUid(),nameText,ToroDataModle.getInstance().getLocalData().getToken());
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button:
                submit();
                break;
            case R.id.edit_head_img:
                showEditPhotoAction();
                break;
        }
    }

    @Override
    public boolean bindData(final int tag, final Object object) {
        switch (tag) {
            case ConnectManager.UPLOAD_PHOTO_LIST:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String result = (String) object;
                        BaseResponeData data = DataModleParser.parserBaseResponeData(result);
                        if(data.isStatus()) {
                            // 上传成功
                            progressView.setProgressText(getString(R.string.upload_head_finish_submit_progress_text));
                            try{
                                JSONArray array = new JSONArray(data.getEntry());
                                JSONObject obj = array.getJSONObject(0);
                                ConnectManager.getInstance().submitPersionalDaitels(EditPersonalDetailsActivity.this,loginUserData.getUid(),nameText, PhotoItem.newInstance(obj),ToroDataModle.getInstance().getLocalData().getToken());
                            } catch (Exception e) {
                                progressView.hide(EditPersonalDetailsActivity.this);
                            }

                        } else {
                            progressView.hide(EditPersonalDetailsActivity.this);
                            Toast.makeText(EditPersonalDetailsActivity.this,getString(R.string.submit_failed),Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case ConnectManager.SUBMIT_PERSIONAL_DETAILS:
                progressView.hide(this);
                boolean status = super.bindData(tag,object);
                if(!status) {
                    Toast.makeText(EditPersonalDetailsActivity.this,getString(R.string.submit_failed),Toast.LENGTH_LONG).show();
                    return status;
                } else {
                    ToroDataModle.getInstance().updateToroLoginUserData();
                    finish();
                    return true;
                }
        }
        return false;
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,EditPersonalDetailsActivity.class);
        return intent;
    }
}