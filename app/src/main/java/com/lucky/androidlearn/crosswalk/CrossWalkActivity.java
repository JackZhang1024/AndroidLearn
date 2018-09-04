package com.lucky.androidlearn.crosswalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lucky.androidlearn.R;

import org.xwalk.core.XWalkActivity;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

public class CrossWalkActivity extends XWalkActivity {

    // https://www.cnblogs.com/rookie-26/p/5914092.html
    // https://blog.csdn.net/nmyangmo/article/details/73105712
    private LinearLayout llContent;
    private XWalkView xWalkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crosswalk);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        xWalkView = new XWalkView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        xWalkView.setLayoutParams(params);
        llContent.addView(xWalkView);
    }


    @Override
    protected void onXWalkReady() {
        initSettings();
        xWalkView.loadUrl("https://www.baidu.com/");
    }

    private void initSettings() {
        XWalkSettings mWebSettings = xWalkView.getSettings();
        mWebSettings.setSupportZoom(true);//支持缩放
        mWebSettings.setBuiltInZoomControls(true);//可以任意缩放
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);////将图片调整到适合webview的大小
//        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setLoadsImagesAutomatically(true);
//        mWebSettings.setMixedContentMode()
        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        mWebSettings.setJavaScriptEnabled(true);//支持JS
    }

    @Override
    protected void onXWalkFailed() {
        super.onXWalkFailed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        llContent.removeView(xWalkView);
    }
}



