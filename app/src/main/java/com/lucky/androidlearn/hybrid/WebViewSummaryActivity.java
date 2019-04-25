package com.lucky.androidlearn.hybrid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 *
 * 总结WebView的使用方法
 *
 */
public class WebViewSummaryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_summary);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_applink)
    public void onWebViewAppLinkClick(){
        Intent intent = new Intent(this, WebViewAppLinkActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_webview_js)
    public void onWebViewJSClick(){
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_webview_newdetail)
    public void onWebViewNewsDetailClick(){
        Intent intent = new Intent(this, WebViewNewsDetailActivity.class);
        startActivity(intent);
    }


}
