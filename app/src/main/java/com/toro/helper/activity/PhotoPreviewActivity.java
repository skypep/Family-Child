package com.toro.helper.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.toro.helper.R;
import com.toro.helper.app.AppConfig;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.photo.PhotoItem;
import com.toro.helper.utils.CameraUtils;
import com.toro.helper.utils.FileUtils;
import com.toro.helper.utils.ImageLoad;
import com.toro.helper.utils.StringUtils;
import com.toro.helper.view.iphone.IphoneDialogBottomMenu;
import com.toro.helper.view.iphone.MenuItemOnClickListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import helper.phone.toro.com.imageselector.utils.ImageSelector;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

/**
 * Create By liujia
 * on 2018/10/26.
 **/
public class PhotoPreviewActivity extends ToroActivity {

    private static final String EXTRA_POSITION = "extra_position";
    private static final String EXTRA_LIST = "extra_list";

    /**
     * 保存图片需要存储空间权限
     */
    private static String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ViewPager viewPager;
    private TextView headText;
    private List<PhotoItem> photos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.photo_preview_activity);
        viewPager = findViewById(R.id.view_pager);
        headText = findViewById(R.id.heard_text);
        int position = getIntent().getIntExtra(EXTRA_POSITION,1);
        photos =  (List<PhotoItem>) getIntent().getSerializableExtra(EXTRA_LIST);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return photos == null?0:photos.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View adView = LayoutInflater.from(PhotoPreviewActivity.this).inflate(R.layout.photo_preview_item, null);
                PhotoView image = (PhotoView) adView.findViewById(R.id.flaw_img);
                image.setBackgroundColor(Color.BLACK);
//                ImageLoad.newInstance(image).load(photos.get(position),R.mipmap.image_loading);
                ImageLoad.GlidLoad(image,photos.get(position),R.mipmap.image_loading);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                image.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showSaveImageMenu((PhotoView) v);
                        return true;
                    }
                });
                container.addView(adView);
                return adView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                headText.setText(position + 1 + "/" + photos.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        viewPager.setCurrentItem(position, true);
        headText.setText(position + 1 + "/" + photos.size());
    }

    private void showSaveImageMenu(final PhotoView photoView) {
        ArrayList<String> menus = new ArrayList<>();
        menus.add(getString(R.string.save_photo_to_disc));
        menus.add(getString(R.string.save_photo_to_gallery));
        IphoneDialogBottomMenu dialog = new IphoneDialogBottomMenu(PhotoPreviewActivity.this,menus,new MenuItemOnClickListener() {
            @Override
            public void onClickMenuItem(View v, int item_index, String item) {
                if(!checkPermission()) {
                    ActivityCompat.requestPermissions(PhotoPreviewActivity.this,
                            permissions, 0);
                }else if(item.equals(getString(R.string.save_photo_to_disc))) {
                    photoView.buildDrawingCache(true);
                    photoView.buildDrawingCache();
                    Bitmap bitmap = photoView.getDrawingCache();
                    String path = saveBitmapFile(bitmap);
                    if(StringUtils.isEmpty(path)){
                        Toast.makeText(PhotoPreviewActivity.this,getString(R.string.save_photo_failed),Toast.LENGTH_LONG).show();
                    } else {
                        String formatString = getResources().getString(R.string.save_photo_sucsses_format);
                        String text = String.format(formatString, path);
                        Toast.makeText(PhotoPreviewActivity.this,text,Toast.LENGTH_LONG).show();
                    }
                    photoView.setDrawingCacheEnabled(false);
                } else if(item.equals(getString(R.string.save_photo_to_gallery))) {
                    photoView.buildDrawingCache(true);
                    photoView.buildDrawingCache();
                    Bitmap bitmap = photoView.getDrawingCache();
                    String path = saveBitmapGallery(bitmap);
                    if(StringUtils.isEmpty(path)){
                        Toast.makeText(PhotoPreviewActivity.this,getString(R.string.save_photo_failed),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PhotoPreviewActivity.this,getString(R.string.save_photo_to_gallery_sucsses),Toast.LENGTH_LONG).show();
                    }
                    photoView.setDrawingCacheEnabled(false);
                }
            }
        });
        dialog.show();
    }

    private boolean checkPermission(){
        for(String permission:permissions) {
            int hasCameraPermission = ContextCompat.checkSelfPermission(getApplication(),
                    permission);
            if(hasCameraPermission != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    private String saveBitmapGallery(Bitmap bitmap) {
        try{
            String result = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Toro", "Toro Photo");
            File file1 = new File(getRealPathFromURI(Uri.parse(result),this));
            updatePhotoMedia(file1,this);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private String saveBitmapFile(Bitmap bitmap){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.jpg", timeStamp);
        File storageDir = FileUtils.getImageCacheDir();
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageFileName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return "";
        }
        try {
            BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(tempFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //更新图库
    private static void updatePhotoMedia(File file ,Context context){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }
    //得到绝对地址
    private static String getRealPathFromURI(Uri contentUri,Context context) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String fileStr = cursor.getString(column_index);
        cursor.close();
        return fileStr;
    }

    public static Intent createIntent(Context context,List<PhotoItem> photos,int position) {
        Intent intent = new Intent();
        intent.setClass(context,PhotoPreviewActivity.class);
        intent.putExtra(EXTRA_POSITION,position);
        intent.putExtra(EXTRA_LIST,(Serializable)photos);
        return intent;
    }

}
