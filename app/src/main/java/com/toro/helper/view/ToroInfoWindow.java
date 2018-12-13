package com.toro.helper.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.Marker;
import com.toro.helper.R;
import com.toro.helper.modle.photo.PhotoItem;
import com.toro.helper.utils.ImageLoad;
import com.toro.helper.utils.StringUtils;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Create By liujia
 * on 2018/11/1.
 **/
public class ToroInfoWindow implements AMap.InfoWindowAdapter {

    private Context mContext;
    private PhotoItem headPhoto;
    private String poiTitle,time;

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infoWindow = LayoutInflater.from(mContext).inflate(
                R.layout.local_info_window, null);
        RoundnessImageView photoView = infoWindow.findViewById(R.id.head_img);
        if(headPhoto!= null) {
            ImageLoad.GlidLoad(photoView,headPhoto,R.mipmap.default_head);
        } else {
            photoView.setImageResource(R.mipmap.default_head);
        }
        if(StringUtils.isNotEmpty(poiTitle)) {
            ((TextView)infoWindow.findViewById(R.id.poi_title)).setText(poiTitle);
        }
        if(StringUtils.isNotEmpty(time)) {
            ((TextView)infoWindow.findViewById(R.id.time)).setText(time);
        }
        return infoWindow;
    }

    public ToroInfoWindow(Context context) {
        this.mContext = context;
    }

    public void setHeadPhoto(PhotoItem photo) {
        this.headPhoto = photo;
    }

    public void setPioTitle(String title) {
        this.poiTitle = title;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
