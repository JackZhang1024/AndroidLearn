package com.lucky.androidlearn.hybrid;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;


// webview在其他进程中进行使用
// 这样可以重新开辟一个进程 多一块内存空间 不占用原来的内存
// 但是如果需要和这个Activity进行通信的话 需要AIDL技术来是实现
// 今日头条 H5秒开功能分析
// https://blog.csdn.net/jiang19921002/article/details/93229460
public class WebAnotherProcessActivity extends AppCompatActivity {


    private InnerWebView mInnerWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_process);
        mInnerWebView = findViewById(R.id.webview);
        loadUrl();
    }

    private void loadUrl() {
        String url = getIntent().getStringExtra("url");
        mInnerWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }


}
