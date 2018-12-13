package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.modle.FamilyMemberInfo;
import com.toro.helper.modle.data.LocationInfo;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.utils.MapUtil;
import com.toro.helper.utils.PhoneUtils;
import com.toro.helper.view.MainActionBar;
import com.toro.helper.view.MapJumpLayout;
import com.toro.helper.view.ToroInfoWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By liujia
 * on 2018/11/1.
 **/
public class MapActivity extends ToroActivity implements View.OnClickListener {

    private static final String EXTRA_MEMBER_INFO = "extra_user_info";
    private FamilyMemberInfo userInfo;
    private MainActionBar mainActionBar;
    private MapJumpLayout mapJumpLayout;
    private ToroInfoWindow toroInfoWindow;
    private Marker marker;
    private MapView mapView;
    private ImageView refreshImage;
    private List<LocationInfo> locationInfos;
    private LocationInfo locationInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        userInfo = (FamilyMemberInfo) getIntent().getSerializableExtra(EXTRA_MEMBER_INFO);
        toroInfoWindow = new ToroInfoWindow(this);
        toroInfoWindow.setHeadPhoto(userInfo.getUserInfo().getHeadPhoto());

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        mainActionBar = findViewById(R.id.main_action_view);
        mainActionBar.updateView(getString(R.string.action_menu_location), R.mipmap.action_back_icon, R.mipmap.refresh_action_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshTrac();
            }
        });
        refreshImage = mainActionBar.getRightImageView();
        mapJumpLayout = findViewById(R.id.map_jump_layout);
        findViewById(R.id.action_call_layout).setOnClickListener(this);
        findViewById(R.id.action_navigation_layout).setOnClickListener(this);
        findViewById(R.id.action_safeguard_layout).setOnClickListener(this);
        findViewById(R.id.action_trac_layout).setOnClickListener(this);
        refreshTrac();
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

    private void refreshTrac() {
        startRefreshAnim();
        ConnectManager.getInstance().activeMember(this,userInfo.getUserInfo().getPhone(), ToroDataModle.getInstance().getLocalData().getToken());
    }

    private void updateTrac() {
        if(locationInfos == null || locationInfos.size() < 1) {
            Toast.makeText(this,R.string.location_refresh_failed,Toast.LENGTH_LONG).show();
            return;
        }
        locationInfo = locationInfos.get(locationInfos.size() - 1);
        AMap aMap = mapView.getMap();
        toroInfoWindow.setPioTitle(locationInfo.getPoiTitle());
        toroInfoWindow.setTime(locationInfo.getTime());
        aMap.setInfoWindowAdapter(toroInfoWindow);
        LatLng curLatLng = locationInfo.getLatLng();
        if(marker == null) {
            marker = aMap.addMarker(new MarkerOptions().position(curLatLng).snippet("DefaultMarker").icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),R.mipmap.map_mark_icon))));
        }else {
            marker.setPosition(curLatLng);
        }
        marker.showInfoWindow();
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(curLatLng));
    }

    @Override
    public boolean bindData(int tag, Object object) {
        boolean status = super.bindData(tag, object);
        stopRefreshAnim();
        if(status) {
            switch (tag) {
                case ConnectManager.GET_TRAC_DATA:
                    BaseResponeData data = DataModleParser.parserBaseResponeData((String) object);
                    locationInfos = DataModleParser.parserLocations(data.getEntry());
                    updateTrac();
                    break;
                case ConnectManager.ACTIVE_MEMBER:
                    startRefreshAnim();
                    ConnectManager.getInstance().getTracData(this,userInfo.getUserInfo().getSn());
                    break;
            }

        }
        return status;
    }

    public static Intent createIntent(Context context,FamilyMemberInfo memberInfo) {
        Intent intent = new Intent();
        intent.setClass(context,MapActivity.class);
        intent.putExtra(EXTRA_MEMBER_INFO,memberInfo);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_call_layout:
                PhoneUtils.call(this,userInfo.getUserInfo().getPhone());
                break;
            case R.id.action_navigation_layout:
                if(locationInfo != null) {
                    mapJumpLayout.showMapJumpLayout(locationInfo.getLatLng().longitude,locationInfo.getLatLng().latitude,userInfo.getDisplayName());
                }else {
                }
                break;
            case R.id.action_safeguard_layout:
                if(locationInfos == null || locationInfos.size() < 1) {
                    Toast.makeText(this,R.string.location_refresh_failed_safeguard,Toast.LENGTH_LONG).show();
                    return;
                }
                float lng = (float) locationInfos.get(locationInfos.size() - 1).getLatLng().longitude;
                float lat = (float) locationInfos.get(locationInfos.size() - 1).getLatLng().latitude;
                startActivity(SafeguardActivity.createIntent(this,lng,lat,userInfo.getUserInfo().getUid()));
                break;
            case R.id.action_trac_layout:
                startActivity(TracActivity.createIntent(this,userInfo));
                break;
        }
    }

}
