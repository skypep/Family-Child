package com.toro.helper.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import helper.phone.toro.com.imagecache.DiskLruCache;


/**
 * Create By liujia
 * on 2018/10/26.
 **/
public class ImageCache {

    private static ImageCache instance;

    private DiskLruCache mDiskLruCache = null;

    //缓存大小
    private int DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    private String UNIQUENAME = "toro_cache";

    public static ImageCache getInstance() {
        if(instance == null) {
            instance = new ImageCache();
        }
        return instance;
    }

    private ImageCache(){

    }

    public void init(Context context) {
        try {
            File cacheDir = getDiskCacheDir(context, UNIQUENAME);

            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            } else

                //第二个参数我选取APP的版本code。DiskLruCache如果发现第二个参数version不同则销毁缓存
                //第三个参数为1，在写缓存的流时候，newOutputStream(0)，0为索引，类似数组的下标
                mDiskLruCache = DiskLruCache.open(cacheDir, getVersionCode(context), 1, DISK_CACHE_MAX_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /*
     *
     * 当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径，
     * 否则就调用getCacheDir()方法来获取缓存路径。
     * 前者获取到的就是 /sdcard/Android/data/<application package>/cache
     * 而后者获取到的是 /data/data/<application package>/cache 。
     *
     * */
    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        File dir = new File(cachePath + File.separator + uniqueName);

        return dir;
    }

    public String urlToKey(String url) {
        return getMD5(url);
    }

    /*
     * 传入一个字符串String msg，返回Java MD5加密后的16进制的字符串结果。
     * 结果形如：c0e84e870874dd37ed0d164c7986f03a
     */
    public String getMD5(String msg) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.reset();
        md.update(msg.getBytes());
        byte[] bytes = md.digest();

        String result = "";
        for (byte b : bytes) {
            // byte转换成16进制
            result += String.format("%02x", b);
        }

        return result;
    }

    byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    //从DiskLruCache中读取缓存
    public Bitmap readBitmapFromDiskLruCache(String url) {
        DiskLruCache.Snapshot snapShot = null;
        try {
            //把url转换成一个md5字符串，然后以这个md5字符串作为key
            String key = urlToKey(url);

            snapShot = mDiskLruCache.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (snapShot != null) {
            InputStream is = snapShot.getInputStream(0);
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            return bitmap;
        } else
            return null;
    }

    //把byte字节写入缓存DiskLruCache
    public void writeToDiskLruCache(String url, Bitmap bitmap) throws Exception {

        //DiskLruCache缓存需要一个key，我先把url转换成md5字符串，
        //然后以md5字符串作为key键
        String key = urlToKey(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);

        OutputStream os = editor.newOutputStream(0);
        byte[] bytes = Bitmap2Bytes(bitmap);
        os.write(bytes);
        os.flush();
        editor.commit();

        mDiskLruCache.flush();

    }

    //把byte字节写入缓存DiskLruCache
    public void writeToDiskLruCache(String url, byte[] buf) throws Exception {
        //DiskLruCache缓存需要一个key，我先把url转换成md5字符串，
        //然后以md5字符串作为key键
        String key = urlToKey(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);

        OutputStream os = editor.newOutputStream(0);
        os.write(buf);
        os.flush();
        editor.commit();

        mDiskLruCache.flush();

    }


}
