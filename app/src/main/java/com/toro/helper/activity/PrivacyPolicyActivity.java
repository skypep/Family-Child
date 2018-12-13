package com.toro.helper.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.toro.helper.R;
import com.toro.helper.app.App;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.view.MainActionBar;

/**
 * Create By liujia
 * on 2018/11/12.
 **/
public class PrivacyPolicyActivity extends ToroActivity  {

    private MainActionBar mainActionBar;
    private ScrollView localLayout;
    private LinearLayout wvContentLayout;
    private ProgressBar progressBar;
    private WebView wb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy_activity);
        mainActionBar = findViewById(R.id.main_action_view);
        mainActionBar.updateView("用户协议", R.mipmap.action_back_icon, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, null);
        wvContentLayout = findViewById(R.id.wvContentLayout);
        progressBar = findViewById(R.id.mProgressBar);
        showWebView();
    }

    private void showWebView() {

        wb = new WebView(App.getInstance());
        WebSettings webSettings = wb.getSettings();
        //支持js交互,这句代码必须加上,如果不加上,会造成图片不能显示,并且无法与js进行交互
        webSettings.setJavaScriptEnabled(true);
        //自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //支持手势进行放大缩小
        webSettings.setBuiltInZoomControls(true);
        webSettings.setBuiltInZoomControls(true);
        //隐藏放大镜图标
        webSettings.setDisplayZoomControls(false);
        wb.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });
        wb.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wb.setLayoutParams(params);
        wb.loadUrl("http://120.78.174.86:8080/kinship/kinship.html");
        wvContentLayout.addView(wb);
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,PrivacyPolicyActivity.class);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewResource();
    }

    /**
     * Description: release the memory of web view, otherwise it's resource will not be recycle.
     */
    private void clearWebViewResource() {
        wb.removeAllViews();
        ((ViewGroup)wb.getParent()).removeView(wb);
        wb.setTag(null);
        wb.clearHistory();
        wb.destroy();
        wb = null;
    }

}
