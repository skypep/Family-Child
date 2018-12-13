package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.toro.helper.R;
import com.toro.helper.app.AppConfig;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.fragment.photo.MarkMemberAdapter;
import com.toro.helper.fragment.photo.PhotoEditAdapter;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.modle.FamilyMemberInfo;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.modle.data.listener.FamilyMemberDataOnChangeListener;
import com.toro.helper.utils.CameraUtils;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.utils.FileUtils;
import com.toro.helper.utils.StringUtils;
import com.toro.helper.utils.okhttp.mutifile.listener.impl.UIProgressListener;
import com.toro.helper.view.MainActionBar;
import com.toro.helper.view.RecyclerItemDecoration;
import com.toro.helper.view.ToroProgressView;
import com.toro.helper.view.iphone.IphoneDialogBottomMenu;
import com.toro.helper.view.iphone.MenuItemOnClickListener;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import helper.phone.toro.com.imageselector.utils.ImageSelector;

/**
 * Create By liujia
 * on 2018/10/24.
 **/
public class UploadPhotoActivity extends ToroActivity implements View.OnClickListener,FamilyMemberDataOnChangeListener {

    private static final String EXTRA_IMAGES = "extra_images";

    private MainActionBar actionBar;
    private Button submitBt;
    private RecyclerView recyclerView,memberRecycler;
    private ArrayList<String> images;
    private PhotoEditAdapter adapter;
    private TextView memberEmpty;

    private ToroProgressView progressView;
    private String mPhotoPath;
    private List<FamilyMemberInfo> members;
    private MarkMemberAdapter memberAdapter;
    private List<Integer> markUids;
    private boolean[] markFlags;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_photo_activity);
        actionBar = findViewById(R.id.main_action_view);
        actionBar.updateView(getString(R.string.upload_photo),R.mipmap.action_back_icon, 0,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        },null);
        progressView = findViewById(R.id.toro_progress);
        submitBt = findViewById(R.id.submit_button);
        submitBt.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view);

        memberRecycler = findViewById(R.id.member_recycler);
        LinearLayoutManager ms= new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        memberRecycler.setLayoutManager(ms);
        memberEmpty = findViewById(R.id.member_empty);
        memberAdapter = new MarkMemberAdapter(null);
        memberRecycler.setAdapter(memberAdapter);

        images = new ArrayList<>();
        updateImages(getIntent().getStringArrayListExtra(EXTRA_IMAGES));
        if(images.size() > AppConfig.PhotoMaxCoun) {
            images = (ArrayList<String>) images.subList(0,AppConfig.PhotoMaxCoun - 1);
        }
        adapter = new PhotoEditAdapter(this,images,plusOnclickListener,deleteOnclickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, AppConfig.PhotoSpanCount));
        recyclerView.addItemDecoration(new RecyclerItemDecoration(this.getResources().getDimensionPixelOffset(R.dimen.photo_list_photo_offset)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMemberList();
        ToroDataModle.getInstance().addToroDataModeOnChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ToroDataModle.getInstance().removeToroDataModeOnChangeListener(this);
    }

    private void updateMemberList() {
        members = ToroDataModle.getInstance().getFamilyMemberData().getFamilyMemberDatas();
        if (members != null && members.size() > 1){
            List<FamilyMemberInfo> list = new ArrayList<>();
            for (FamilyMemberInfo info : members) {
                if (info.getStatus() == 0){
                    list.add(info);
                }
            }
            members = list;
        }
        if(members == null || members.size() < 1) {
            memberRecycler.setVisibility(View.GONE);
            memberEmpty.setVisibility(View.VISIBLE);
        }else {
            memberRecycler.setVisibility(View.VISIBLE);
            memberEmpty.setVisibility(View.GONE);
            markFlags = new boolean[members.size()];
            memberAdapter.updateDatas(members,markFlags);
            memberAdapter.setOnClickListener(markOncliListener);
        }
    }

    private void updateImages(ArrayList<String> images) {
        if(images == null || images.size() < 1) {
            submitBt.setEnabled(false);
        } else {
            this.images = images;
            submitBt.setEnabled(true);
        }
    }

    public static Intent newIntent(Context context, ArrayList<String> images) {
        Intent intent = new Intent();
        intent.setClass(context,UploadPhotoActivity.class);
        intent.putStringArrayListExtra(EXTRA_IMAGES,images);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                submit();
                break;
        }
    }

    private void submit() {
        if(images == null || images.size() < 1) {
            Toast.makeText(this,R.string.photo_is_empty,Toast.LENGTH_LONG).show();
            return;
        }
        submitBt.setClickable(false);
        showProgress(0);
        uploadImages(images);
    }

    private void uploadImages(ArrayList<String> images) {
        ConnectManager.getInstance().uploadPhotos(this,this, images, ToroDataModle.getInstance().getLocalData().getToken(), new UIProgressListener() {
            @Override
            public void onUIProgress(long currentBytes, long contentLength, boolean done) {
                showProgress((int) (currentBytes * 100 / contentLength));
            }
        });
    }

    private void showProgress(int progress) {
        progressView.show(this);
        String formatString = getResources().getString(R.string.upload_progress_text_format);
        String text = String.format(formatString, progress + "%");
        progressView.setProgressText(text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraUtils.PHOTO_REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            for(String image : images) {
                this.images.add(image);
            }
            adapter.updateListData(this.images);
        }else if (requestCode == CameraUtils.CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if(StringUtils.isNotEmpty(mPhotoPath)) {
                    this.images.add(mPhotoPath);
                    adapter.updateListData(this.images);
                }
            }
        }
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


    private View.OnClickListener plusOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<String> menus = new ArrayList<>();
            menus.add(getString(R.string.take_photo_by_camera));
            menus.add(getString(R.string.choose_photo_from_album));
            IphoneDialogBottomMenu dialog = new IphoneDialogBottomMenu(UploadPhotoActivity.this,menus,new MenuItemOnClickListener() {
                @Override
                public void onClickMenuItem(View v, int item_index, String item) {
                    if(item.equals(getString(R.string.take_photo_by_camera))) {
                        CameraUtils.checkPermissionAndCamera(UploadPhotoActivity.this, new CameraUtils.OnCameraPermissionListener() {
                            @Override
                            public void onHasePermission() {
                                mPhotoPath = CameraUtils.openCamera(UploadPhotoActivity.this);
                            }
                        });
                    } else if(item.equals(getString(R.string.choose_photo_from_album))){
                        ImageSelector.builder()
                                .useCamera(true) // 设置是否使用拍照
                                .setSingle(false)  //设置是否单选
                                .setViewImage(true) //是否点击放大图片查看,，默认为true
                                .setMaxSelectCount(AppConfig.PhotoMaxCoun - images.size()) // 图片的最大选择数量，小于等于0时，不限数量。
                                .start(UploadPhotoActivity.this, CameraUtils.PHOTO_REQUEST_CODE); // 打开相册

                    }
                }
            });
            dialog.show();
        }
    };

    private View.OnClickListener deleteOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                int index = (int) v.getTag();
                if(index >=0 && index < images.size()) {
                    images.remove(index);
                    adapter.updateListData(images);
                    if(images.size() < 1) {
                        submitBt.setEnabled(false);
                    }
                }
            }catch (Exception e){

            }

        }
    };

    private void submitFinish() {
        submitBt.setClickable(true);
        progressView.hide(UploadPhotoActivity.this);
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
                            progressView.setProgressText(getString(R.string.upload_finish_submit_progress_text));
                            markUids = new ArrayList<>();
                            if (markFlags != null) {
                                for (int i = 0; i < markFlags.length; i++) {
                                    if (markFlags[i]) {
                                        markUids.add(members.get(i).getId());
                                    }
                                }
                            }
                            ConnectManager.getInstance().submitPhotoList(UploadPhotoActivity.this,DataModleParser.parserPhotoItems(data.getEntry()),markUids,"",1,ToroDataModle.getInstance().getLocalData().getToken());
                        } else {
                            submitFinish();
                            Toast.makeText(UploadPhotoActivity.this,getString(R.string.submit_failed),Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case ConnectManager.SUBMIT_PHOTO_LIST:
                submitFinish();
                boolean status = super.bindData(tag,object);
                if(!status) {
                    Toast.makeText(UploadPhotoActivity.this,getString(R.string.submit_failed),Toast.LENGTH_LONG).show();
                    return status;
                } else {
                    Toast.makeText(UploadPhotoActivity.this,getString(R.string.submit_sucsses),Toast.LENGTH_LONG).show();
                    ToroDataModle.getInstance().updateToroFamilyPhotoList();
                    Intent intent = new Intent();
                    intent.putExtra(MyPhotoActivity.UPLOAD_REQUEST_EXTRA, true);
                    setResult(RESULT_OK, intent);
                    finish();
                    return true;
                }
        }
        return false;
    }

    private View.OnClickListener markOncliListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            markFlags[index] = !markFlags[index];
            memberAdapter.updateDatas(members,markFlags);
        }
    };

    @Override
    public void onChange(Object obj) {
        updateMemberList();
    }
}
