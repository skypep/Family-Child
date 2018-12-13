//package com.toro.helper.utils;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.GlideBuilder;
//import com.bumptech.glide.Registry;
//import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
//import com.bumptech.glide.load.model.GlideUrl;
//import com.bumptech.glide.module.GlideModule;
//import com.toro.helper.modle.ToroUserManager;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.annotation.Annotation;
//
//import okhttp3.Interceptor;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
///**
// * Create By liujia
// * on 2018/10/27.
// **/
//public class OkHttpGlideModule implements GlideModule {
//
//
//    @Override
//    public void applyOptions(Context context, GlideBuilder builder) {
//        int i = 0;
//        i ++;
//    }
//
//    @Override
//    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
//        //定制OkHttp
//        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
//
//        //请求头设置
//        httpClientBuilder.interceptors().add(new HeadInterceptor(context));
//
//        OkHttpClient okHttpClient = httpClientBuilder.build();
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
//    }
//
//    public static class HeadInterceptor implements Interceptor {
//        Context mcontext;
//        public HeadInterceptor(Context context) {
//            mcontext = context;
//        }
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request.Builder request = chain.request().newBuilder();
//
//            //这里添加我们需要的请求头
//            request.addHeader("Authorization", ToroUserManager.getInstance(mcontext).getToken());
//            request.addHeader("path", "e/3/5/d/8/0/d/b/");
//            return chain.proceed(request.build());
//        }
//    }
//}
