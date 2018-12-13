package com.toro.helper.activity;

import android.app.ProgressDialog;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.modle.data.LocationInfo;
import com.toro.helper.modle.data.SafeguardInfo;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.view.MainActionBar;

import org.json.JSONObject;

/**
 * Create By liujia
 * on 2018/11/3.
 **/
public class SafeguardActivity extends ToroActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    private static final String EXTRA_LNG = "extra_lng";
    private static final String EXTRA_LAT = "extra_lat";
    private static final String EXTRA_UID = "extra_uid";

    private MainActionBar mainActionBar;
    private long lng,lat;
    private int uid;
    private LinearLayout settingLayout;
    private RelativeLayout mapLayout;
    private MapView mapView;
    private Marker marker;
    private Circle circle;
    private SafeguardInfo safeguardInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safeguard_activity);
        lng = getIntent().getLongExtra(EXTRA_LNG,0);
        lat = getIntent().getLongExtra(EXTRA_LAT,0);
        uid = getIntent().getIntExtra(EXTRA_UID,0);
        mainActionBar = findViewById(R.id.main_action_view);
        mainActionBar.updateView(getString(R.string.map_action_safeguard), R.mipmap.action_back_icon, R.mipmap.action_setting_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SafeguardSettingActivity.createIntent(SafeguardActivity.this,safeguardInfo,ToroDataModle.getInstance().getFamilyMemberData().getMemberInfoByUid(uid).getUserInfo().getPhone()));
            }
        });

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        initView();
        progressDialog = ProgressDialog.show(this,"",getString(R.string.loading));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(safeguardInfo == null) {
            updateConfig();
        }
    }

    private void updateConfig() {
        progressDialog.show();
        ConnectManager.getInstance().getSafeguard(this,ToroDataModle.getInstance().getFamilyMemberData().getMemberInfoByUid(uid).getUserInfo().getPhone(),ToroDataModle.getInstance().getLocalData().getToken());
    }

    private void initView() {
        settingLayout = findViewById(R.id.setting_layout);
        mapLayout = findViewById(R.id.map_layout);
        findViewById(R.id.setting_button).setOnClickListener(this);
    }

    private void showSettingLayout() {
        settingLayout.setVisibility(View.VISIBLE);
        mapLayout.setVisibility(View.GONE);
    }

    private void showMapLayout(LatLng homeLatLng,long safeguardRadius) {
        mapLayout.setVisibility(View.VISIBLE);
        settingLayout.setVisibility(View.GONE);

        AMap aMap = mapView.getMap();
        LatLng latLng = new LatLng(lat,lng);
        if(marker == null) {
            marker = aMap.addMarker(new MarkerOptions().position(latLng).snippet("DefaultMarker").icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),R.mipmap.map_mark_icon))));
        }else {
            marker.setPosition(latLng);
        }

        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(homeLatLng));

        if(circle == null) {
            circle = aMap.addCircle(new CircleOptions().
                    center(homeLatLng).
                    radius(safeguardRadius).
                    fillColor(Color.argb(100, 255, 1, 1)).
                    strokeColor(Color.argb(100, 255, 1, 1)).
                    strokeWidth(1));
        } else {
            circle.setCenter(homeLatLng);
            circle.setRadius(safeguardRadius);
        }

    }

    public static Intent createIntent(Context context,float lng,float lat,int uid) {
        Intent intent = new Intent();
        intent.setClass(context,SafeguardActivity.class);
        intent.putExtra(EXTRA_LNG,lng);
        intent.putExtra(EXTRA_LAT,lat);
        intent.putExtra(EXTRA_UID,uid);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_button:
                startActivity(SettingHomeLoactionActivity.createIntent(this,ToroDataModle.getInstance().getFamilyMemberData().getMemberInfoByUid(uid).getUserInfo().getPhone(),safeguardInfo));
                break;
        }
    }

    private void parseSafeConfig(String result) {
        safeguardInfo = SafeguardInfo.newInstance(result);
        if(safeguardInfo == null || safeguardInfo.getLocationInfo() == null || safeguardInfo.getScope() == 0) {
            showSettingLayout();
        } else {
            showMapLayout(safeguardInfo.getLocationInfo().getLatLng(),safeguardInfo.getScope());
        }
    }

    @Override
    public boolean bindData(int tag, Object object) {
        boolean status = super.bindData(tag, object);
        BaseResponeData data = DataModleParser.parserBaseResponeData((String) object);
        switch (tag) {
            case ConnectManager.GET_SAFEGUARD:
                progressDialog.dismiss();
                if(data == null) {
                    Toast.makeText(this,R.string.safeguard_update_failed,Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    parseSafeConfig(data.getEntry());
                }
                break;
        }
        return status;
    }
}
