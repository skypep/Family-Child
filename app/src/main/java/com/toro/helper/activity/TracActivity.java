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
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.modle.FamilyMemberInfo;
import com.toro.helper.modle.data.LocationInfo;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.view.MainActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By liujia
 * on 2018/11/6.
 **/
public class TracActivity extends ToroActivity {

    private MainActionBar mainActionBar;
    private MapView mapView;
    private Marker marker;
    private List<LocationInfo> locationInfos;
    private Polyline tracLine;
    private ImageView refreshImage;
    private static final String EXTRA_MEMBER_INFO = "extra_user_info";
    private FamilyMemberInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trac_activity);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mainActionBar = findViewById(R.id.main_action_view);
        mainActionBar.updateView(getString(R.string.map_action_trac), R.mipmap.action_back_icon, R.mipmap.refresh_action_icon, new View.OnClickListener() {
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
        userInfo = (FamilyMemberInfo) getIntent().getSerializableExtra(EXTRA_MEMBER_INFO);
        refreshTrac();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        AMap aMap = mapView.getMap();
        List<LatLng> latLngs = new ArrayList<LatLng>();
        for(LocationInfo info:locationInfos) {
            latLngs.add(info.getLatLng());
        }
        if(tracLine == null) {
            tracLine = aMap.addPolyline(new PolylineOptions().
                    addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
        } else {
            tracLine.setPoints(latLngs);
        }
        LatLng curLatLng = locationInfos.get(locationInfos.size() - 1).getLatLng();
        if(marker == null) {
            marker = aMap.addMarker(new MarkerOptions().position(curLatLng).snippet("DefaultMarker").icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),R.mipmap.map_mark_icon))));
        }else {
            marker.setPosition(curLatLng);
        }
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
        intent.setClass(context,TracActivity.class);
        intent.putExtra(EXTRA_MEMBER_INFO,memberInfo);
        return intent;
    }
}
