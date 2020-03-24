package com.lucky.androidlearn.hybrid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.hybrid.sonic.SonicJavaScriptInterface;
import com.lucky.androidlearn.hybrid.sonic.SonicRuntimeImpl;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSessionConfig;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 总结WebView的使用方法
 */
public class WebViewSummaryActivity extends AppCompatActivity {

    public static final int MODE_DEFAULT = 0;

    public static final int MODE_SONIC = 1;

    public static final int MODE_SONIC_WITH_OFFLINE_CACHE = 2;

    private static final int PERMISSION_REQUEST_CODE_STORAGE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_summary);
        ButterKnife.bind(this);
        init();
    }


    @OnClick(R.id.btn_applink)
    public void onWebViewAppLinkClick() {
        Intent intent = new Intent(this, WebViewAppLinkActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_webview_js)
    public void onWebViewJSClick() {
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_webview_newdetail)
    public void onWebViewNewsDetailClick() {
        Intent intent = new Intent(this, WebViewNewsDetailActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_webview_process)
    public void onWebViewProcessDetailClick() {
        Intent intent = new Intent(this, WebAnotherProcessActivity.class);
        intent.putExtra("url", "http://www.baidu.com");
        startActivity(intent);
    }

    @OnClick(R.id.btn_vsonic)
    public void onVsonicClick() {
       startBrowserActivity(MODE_SONIC, "http://www.baidu.com");
    }

    @OnClick(R.id.btn_preload)
    public void preLoad(){
        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
        sessionConfigBuilder.setSupportLocalServer(true);

        // preload session
        boolean preloadSuccess = SonicEngine.getInstance().preCreateSession("http://www.baidu.com", sessionConfigBuilder.build());
        Toast.makeText(getApplicationContext(), preloadSuccess ? "Preload start up success!" : "Preload start up fail!", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_offline)
    public void offline(){
        startBrowserActivity(MODE_SONIC_WITH_OFFLINE_CACHE, "http://www.baidu.com");
    }

    private void init() {
        // init sonic engine
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
        }
    }

    private void startBrowserActivity(int mode, String url) {
        Intent intent = new Intent(this, VsonicBrowserActivity.class);
        intent.putExtra(VsonicBrowserActivity.PARAM_URL, url);
        intent.putExtra(VsonicBrowserActivity.PARAM_MODE, mode);
        intent.putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis());
        startActivityForResult(intent, -1);
    }

}
