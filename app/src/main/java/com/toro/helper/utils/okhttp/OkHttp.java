package com.toro.helper.utils.okhttp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.toro.helper.utils.ImageCache;
import com.toro.helper.utils.ImageLoad;
import com.toro.helper.utils.OnHttpDataUpdateListener;
import com.toro.helper.utils.okhttp.mutifile.listener.impl.UIProgressListener;
import com.toro.helper.utils.okhttp.mutifile.utils.OKHttpUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.zibin.luban.Luban;

/**
 * Create By liujia
 * on 2018/10/24.
 **/
public class OkHttp {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String doPost(String url, JSONObject obj) {
        try{
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, obj.toString());
            Request request = new Request.Builder()
                    .header("Content-Type","application/json")
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String doTokenGet(String url,String token,int offset,int limit) {
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("Authorization", token)
                    .addHeader("Content-Type","application/json;charset=utf-8")
                    .addHeader("pageIndex",offset + "")
                    .addHeader("pageSize",limit + "")
                    .url(url)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String doTokenGet(String url,String token) {
        OkHttpClient okHttpClient = getToroOkHttpClient(token);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String doTokenGet(String url) {
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("Content-Type","application/json;charset=utf-8")
                    .url(url)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String doTokenPut(String url, JSONObject obj,String token) {
        try{
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, obj.toString());
            Request request = new Request.Builder()
                    .addHeader("Authorization", token)
                    .addHeader("Content-Type","application/json;charset=utf-8")
                    .url(url)
                    .put(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String doTokenPost(String url, JSONObject obj,String token) {
        try{
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, obj.toString());
            Request request = new Request.Builder()
                    .addHeader("Authorization", token)
                    .addHeader("Content-Type","application/json;charset=utf-8")
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void upLoadFiles(final Context context,final int tag, String url, ArrayList<String>fileNames, String token, UIProgressListener progressListener, final OnHttpDataUpdateListener listener) {
        try{
            /**
             * 上传前压缩压缩
             * 既然压缩压缩，那么gif就不能动咯
             */
            List<File> files = Luban.with(context).load(fileNames).get();
            if(files.size() == fileNames.size()) {
                for(int i = 0; i < files.size(); i++) {
                    int length = (int) (files.get(i).length()/1024);
                    fileNames.set(i,files.get(i).getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        OKHttpUtils.doToroUploadFilesRequest(getToroOkHttpClient(token),url, fileNames, progressListener, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                listener.bindData(tag,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.bindData(tag,response.body().string());
            }
        });
    }

    public static void upLoadFile(Context context,final int tag, String url, String fileName, String token, UIProgressListener progressListener, final OnHttpDataUpdateListener listener) {
        try{
            /**
             * 上传前压缩压缩
             * 既然压缩压缩，那么gif就不能动咯
             */
            List<File> files = Luban.with(context).load(fileName).get();
            if(files != null && files.size() == 1) {
                fileName = files.get(0).getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        OKHttpUtils.doToroUploadFileRequest(getToroOkHttpClient(token),url, fileName, progressListener, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                listener.bindData(tag,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.bindData(tag,response.body().string());
            }
        });
    }

    /**
     * 创建一个OkHttpClient的对象的单例
     *
     * @return
     */
    private static OkHttpClient getToroOkHttpClient(final String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //设置连接超时等属性,不设置可能会报异常
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS)
                .writeTimeout(5000, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type","application/json")
                                .addHeader("Authorization", token).build();
                        return chain.proceed(request);
                    }
                });

        OkHttpClient client = builder.build();
        return client;
    }

    private static OkHttpClient getToroOkHttpClient(final String token,final String path) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //设置连接超时等属性,不设置可能会报异常
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS)
                .writeTimeout(5000, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type","application/json")
                                .addHeader("path", path)
                                .addHeader("Authorization", token).build();
                        return chain.proceed(request);
                    }
                });

        OkHttpClient client = builder.build();
        return client;
    }

    public static void downloadImage(final int tag,final String url,String token,String path,final OnHttpDataUpdateListener listener) {
        Request request=new Request.Builder()
                .url(url)
                .build();
        Call call = getToroOkHttpClient(token,path).newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                listener.bindData(tag,e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //获取输出流，然后通过BitmapFactory生成Bitmap
//                InputStream is = response.body().byteStream();
//                final Bitmap bitmap= BitmapFactory.decodeStream(is);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final byte[] bytes = response.body().bytes();
                            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            listener.bindData(tag,bitmap);
                            ImageCache.getInstance().writeToDiskLruCache(url, bytes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }

}
