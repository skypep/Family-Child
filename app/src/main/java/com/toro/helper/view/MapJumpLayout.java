package com.toro.helper.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.toro.helper.R;
import com.toro.helper.utils.MapUtil;
import com.toro.helper.utils.StringUtils;

/**
 * Create By liujia
 * on 2018/11/2.
 **/
public class MapJumpLayout extends LinearLayout implements View.OnClickListener {

    private LinearLayout gdLayout,bdLayout,txLayout;
    private ImageView gdImg,bdImg,txImg;
    private TextView gdText,bdText,txText;
    private int mapCount;
    private double longitude,latitude;
    private String addressName;// 目的地名字

    public MapJumpLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        gdLayout = findViewById(R.id.gd_layout);
        bdLayout = findViewById(R.id.bd_layout);
        txLayout = findViewById(R.id.tx_layout);

        gdImg = findViewById(R.id.gd_img);
        bdImg = findViewById(R.id.bd_img);
        txImg = findViewById(R.id.tx_img);

        gdText = findViewById(R.id.gd_text);
        bdText = findViewById(R.id.bd_text);
        txText = findViewById(R.id.tx_text);

        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.gd_layout).setOnClickListener(this);
        findViewById(R.id.bd_layout).setOnClickListener(this);
        findViewById(R.id.tx_layout).setOnClickListener(this);
    }

    public void setUpView() {
        mapCount = 0;
        if(MapUtil.isGdMapInstalled()){
            Drawable drawable = MapUtil.getGdMapIcon(getContext());
            String appName = MapUtil.getGdMapAppName(getContext());
            if(drawable == null || StringUtils.isEmpty(appName)) {
                gdLayout.setVisibility(View.GONE);
            } else {
                mapCount ++;
                gdLayout.setVisibility(View.VISIBLE);
                gdImg.setImageDrawable(drawable);
                gdText.setText(appName);
            }
        } else {
            gdLayout.setVisibility(View.GONE);
        }

        if(MapUtil.isBaiduMapInstalled()){
            Drawable drawable = MapUtil.getBaiduMapIcon(getContext());
            String appName = MapUtil.getBaiduMapName(getContext());
            if(drawable == null || StringUtils.isEmpty(appName)) {
                bdLayout.setVisibility(View.GONE);
            } else {
                mapCount ++;
                bdLayout.setVisibility(View.VISIBLE);
                bdImg.setImageDrawable(drawable);
                bdText.setText(appName);
            }
        } else {
            bdLayout.setVisibility(View.GONE);
        }

        if(MapUtil.isTencentMapInstalled()){
            Drawable drawable = MapUtil.getQQMapIcon(getContext());
            String appName = MapUtil.getQQMapName(getContext());
            if(drawable == null || StringUtils.isEmpty(appName)) {
                txLayout.setVisibility(View.GONE);
            } else {
                mapCount ++;
                txLayout.setVisibility(View.VISIBLE);
                txImg.setImageDrawable(drawable);
                txText.setText(appName);
            }
        }else {
            txLayout.setVisibility(View.GONE);
        }
    }

    public void showMapJumpLayout(double longitude,double latitude,String addressName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressName = addressName;
        setUpView();
        if(mapCount > 0) {
            setVisibility(VISIBLE);
        } else {
            Toast.makeText(getContext(),R.string.please_install_map,Toast.LENGTH_LONG).show();
        }
    }

    public void hideMapJumpLayout() {
        setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                hideMapJumpLayout();
                break;
            case R.id.gd_layout:
                MapUtil.openGaoDeNavi(getContext(),0,0,null,latitude,longitude,addressName);
                break;
            case R.id.bd_layout:
                MapUtil.openBaiDuNavi(getContext(),0,0,null,latitude,longitude,addressName);
                break;
            case R.id.tx_layout:
                MapUtil.openTencentMap(getContext(),0,0,null,latitude,longitude,addressName);
                break;
        }
    }
}
