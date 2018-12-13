package com.toro.helper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.modle.photo.PhotoItem;
import com.toro.helper.utils.okhttp.OkHttp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create By liujia
 * on 2018/10/26.
 **/
public class ImageLoad {

    private String url;

    private static ImageLoad newInstance(ImageView imageView) {
        ImageLoad instance = new ImageLoad(imageView);

        return instance;
    }

    public static void GlidLoad(ImageView imageView,PhotoItem photo,int defaultRes) {
        if(photo == null) {
            imageView.setImageResource(defaultRes);
            return;
        }
        String url = ConnectManager.getInstance().getPhotoUrl(photo);
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", ToroDataModle.getInstance().getLocalData().getToken())
                .addHeader("path", photo.getPath())
                .build());
        RequestOptions options = new RequestOptions();
        options.placeholder(defaultRes);
        options.error(defaultRes);
        Glide.with(imageView).load(glideUrl)
                .apply(options)
                .into(imageView);
    }

    public static void GlidLoad(ImageView imageView,String localPath,int defaultRes) {
        if(StringUtils.isEmpty(localPath)) {
            imageView.setImageResource(defaultRes);
            return;
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(defaultRes);
        options.error(defaultRes);
        Glide.with(imageView).load(localPath)
                .apply(options)
                .into(imageView);
    }

    private ImageView imageView;
    private Context mContext;

    private ImageLoad(ImageView imageView) {
        this.imageView = imageView;
        mContext = imageView.getContext();
    }

    public void load(PhotoItem photo, int defultResId) {
        if(photo == null) {
            imageView.setImageResource(defultResId);
        } else {
            url = ConnectManager.getInstance().getPhotoUrl(photo);
            Bitmap bitmap = ImageCache.getInstance().readBitmapFromDiskLruCache(url);
            if(bitmap == null) {
                imageView.setImageResource(defultResId);
                OkHttp.downloadImage(ConnectManager.DOWNLOAD_IMAGE,url, ToroDataModle.getInstance().getLocalData().getToken(),photo.getPath(),listener);
            } else {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private OnHttpDataUpdateListener listener = new OnHttpDataUpdateListener() {
        @Override
        public boolean bindData(int tag, Object object) {
            if(tag == ConnectManager.DOWNLOAD_IMAGE) {
                try{
                    final Bitmap bitmap = (Bitmap) object;
                    if(bitmap != null) {
                        imageView.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    };
}
