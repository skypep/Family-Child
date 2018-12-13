package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.modle.FamilyMemberInfo;
import com.toro.helper.modle.data.LocationInfo;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.modle.data.listener.FamilyMemberDataOnChangeListener;
import com.toro.helper.utils.CameraUtils;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.utils.ImageLoad;
import com.toro.helper.utils.PhoneUtils;
import com.toro.helper.view.MainActionBar;
import com.toro.helper.view.RoundnessImageView;

import java.util.List;

/**
 * Create By liujia
 * on 2018/11/1.
 **/
public class HelperActivity extends ToroActivity implements View.OnClickListener ,FamilyMemberDataOnChangeListener {

    private static final String EXTRA_MEMBER_INFO = "extra_user_info";

    private MainActionBar mainActionBar;
    private FamilyMemberInfo userInfo;

    private RoundnessImageView headImageView;
    private TextView nameText;
    private ImageView refreshImage;
    private List<LocationInfo> locationInfos;
    private int connectCount = 0;
    private TextView activeTx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helper_activity);
        userInfo = (FamilyMemberInfo) getIntent().getSerializableExtra(EXTRA_MEMBER_INFO);
        if(userInfo == null) {
            finish();
            return;
        }
        initView();
        updateMemberInfo();
        refreshPhoneStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToroDataModle.getInstance().addToroDataModeOnChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ToroDataModle.getInstance().removeToroDataModeOnChangeListener(this);
    }

    private void initView() {
        mainActionBar = findViewById(R.id.main_action_view);
        headImageView = findViewById(R.id.head_img);
        nameText = findViewById(R.id.name_text);
        activeTx = findViewById(R.id.active);

        refreshImage = findViewById(R.id.refresh_img);
        refreshImage.setOnClickListener(this);

        View setting1 = findViewById(R.id.setting_item1);
        View setting2 = findViewById(R.id.setting_item2);
        View setting3 = findViewById(R.id.setting_item3);
        View setting4 = findViewById(R.id.setting_item4);

        setItemView(setting1,R.string.action_menu_health,R.mipmap.action_menu_health,null);
        setItemView(setting2,R.string.action_menu_location,R.mipmap.action_menu_localtion,null);
        setItemView(setting3,R.string.action_menu_call,R.mipmap.action_menu_call,null);
        setItemView(setting4,R.string.map_action_safeguard,R.mipmap.action_menu_safeguard,null);

        setting1.setOnClickListener(this);
        setting2.setOnClickListener(this);
        setting3.setOnClickListener(this);
        setting4.setOnClickListener(this);
        activeTx.setOnClickListener(this);

    }

    private void updateMemberInfo() {
        userInfo = ToroDataModle.getInstance().getFamilyMemberData().getMemberInfoByid(userInfo.getId());
        String titile = "";
        if(userInfo != null) {
            titile = userInfo.getDisplayName();
        }
        if(userInfo != null && userInfo.getUserInfo() != null && userInfo.getUserInfo().getHeadPhoto() != null) {
            ImageLoad.GlidLoad(headImageView,userInfo.getUserInfo().getHeadPhoto(),R.mipmap.default_head);
        }else {
            headImageView.setImageResource(R.mipmap.default_head);
        }
        mainActionBar.updateView(titile, R.mipmap.action_back_icon, R.mipmap.icon_personal, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FamilyMemberEditActivity.createAddIntent(HelperActivity.this,userInfo));
            }
        });
        nameText.setText(userInfo.getDisplayName());
    }

    private void refreshPhoneStatus() {
        startRefreshAnim();
//        ConnectAction(TYPE_PHONE_STATUS); // 手机状态功能暂时拿掉
        ConnectAction(TYPE_TRAC_DATA);
        ConnectAction(TYPE_MEMBER_STATUS);
        ConnectAction(TYPE_ACTIVE_MEMBER);
    }

    private void updateLocation() {
        if(locationInfos == null || locationInfos.size() < 1) {
            Toast.makeText(this,R.string.location_refresh_failed,Toast.LENGTH_LONG).show();
            return;
        }
        ((TextView)findViewById(R.id.location_text)).setText(locationInfos.get(locationInfos.size() - 1).getPoiTitle());
    }

    private void setItemView(View itemView, int stringID, int imgID, View.OnClickListener listener) {
        ((ImageView)itemView.findViewById(R.id.title_icon)).setImageResource(imgID);
        ((TextView)itemView.findViewById(R.id.title_text)).setText(stringID);
        itemView.setOnClickListener(listener);
    }

    private void startRefreshAnim() {
        refreshImage.setClickable(false);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.refresh_anim);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        refreshImage.startAnimation(animation);
    }

    private void stopRefreshAnim() {
        refreshImage.setClickable(true);
        refreshImage.clearAnimation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PhoneUtils.REQUEST_CALL_PERMISSION: //拨打电话
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                } else {//成功
                    PhoneUtils.call(this,userInfo.getUserInfo().getPhone());
                }
                break;
        }
    }

    public static Intent createIntent(Context context, FamilyMemberInfo memberInfo) {
        Intent intent = new Intent();
        intent.setClass(context,HelperActivity.class);
        intent.putExtra(EXTRA_MEMBER_INFO,memberInfo);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_item1:
                startActivity(HealthActivity.createIntent(this,userInfo.getUserInfo().getUid()));
                break;
            case R.id.setting_item2:
                startActivity(MapActivity.createIntent(this,userInfo));
                break;
            case R.id.setting_item3:
                PhoneUtils.call(this,userInfo.getUserInfo().getPhone());
                break;
            case R.id.setting_item4:
                if(locationInfos == null || locationInfos.size() < 1) {
                    Toast.makeText(this,R.string.location_refresh_failed_safeguard,Toast.LENGTH_LONG).show();
                    return;
                }
                float lng = (float) locationInfos.get(locationInfos.size() - 1).getLatLng().longitude;
                float lat = (float) locationInfos.get(locationInfos.size() - 1).getLatLng().latitude;
                startActivity(SafeguardActivity.createIntent(this,lng,lat,userInfo.getUserInfo().getUid()));
                break;
            case R.id.refresh_img:
                refreshPhoneStatus();
                break;
            case R.id.active:
                activeTx.setVisibility(View.GONE);
                startRefreshAnim();
                ConnectAction(TYPE_ACTIVE_MEMBER);
                break;
        }
    }

    private static final int TYPE_PHONE_STATUS = 1;
    private static final int TYPE_TRAC_DATA = 2;
    private static final int TYPE_MEMBER_STATUS = 3;
    private static final int TYPE_ACTIVE_MEMBER = 4;
    private void ConnectAction(int type) {
        switch (type) {
            case TYPE_PHONE_STATUS:
                connectCount++;
                ConnectManager.getInstance().getUserPhoneStatus(this,userInfo.getId(), ToroDataModle.getInstance().getLocalData().getToken());
                break;
            case TYPE_TRAC_DATA:
                connectCount++;
                ConnectManager.getInstance().getTracData(this,userInfo.getUserInfo().getSn());
                break;
            case TYPE_MEMBER_STATUS:
                connectCount++;
                ConnectManager.getInstance().getMemberStatus(this,userInfo.getUserInfo().getPhone(),ToroDataModle.getInstance().getLocalData().getToken());
                break;
            case TYPE_ACTIVE_MEMBER:
                connectCount++;
                ConnectManager.getInstance().activeMember(this,userInfo.getUserInfo().getPhone(),ToroDataModle.getInstance().getLocalData().getToken());
                break;
        }
    }

    @Override
    public boolean bindData(int tag, Object object) {
        connectCount--;
        if(connectCount == 0) {
            stopRefreshAnim();
        }
        boolean status = super.bindData(tag, object);

        if(status) {
            BaseResponeData data = DataModleParser.parserBaseResponeData((String) object);
            switch (tag) {
                case ConnectManager.GET_USER_PHONE_STATUS:
                    break;
                case ConnectManager.GET_TRAC_DATA:
                    locationInfos = DataModleParser.parserLocations(data.getEntry());
                    updateLocation();
                    break;
                case ConnectManager.GET_MEMBER_STATUS:
                    activeTx.setVisibility(View.GONE);
                    break;
                case ConnectManager.ACTIVE_MEMBER:
                    activeTx.setVisibility(View.GONE);
                    startRefreshAnim();
                    ConnectAction(TYPE_MEMBER_STATUS);
                    break;
            }
        }else {
            if(tag == ConnectManager.GET_MEMBER_STATUS || tag == ConnectManager.ACTIVE_MEMBER) {
                activeTx.setVisibility(View.VISIBLE);
            }
        }
        return status;
    }

    @Override
    public void onChange(Object obj) {
        updateMemberInfo();
    }
}
