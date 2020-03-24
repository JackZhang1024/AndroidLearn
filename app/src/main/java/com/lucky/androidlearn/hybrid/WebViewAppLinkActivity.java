package com.lucky.androidlearn.hybrid;

import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;


/*
*ANDROID应用开发的时候可能会用到WEBVIEW这个组件，使用过程中可能会接触到WEBVIEWCLIENT与WEBCHROMECLIENT，那么这两个类到底有什么不同呢？

WebViewClient主要帮助WebView处理各种通知、请求事件的，比如：



onLoadResource

onPageStart
onPageFinish
onReceiveError
onReceivedHttpAuthRequest
WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等比如


onCloseWindow(关闭WebView)

onCreateWindow()
onJsAlert (WebView上alert无效，需要定制WebChromeClient处理弹出)
onJsPrompt
onJsConfirm
onProgressChanged
onReceivedIcon
onReceivedTitle
看上去他们有很多不同，实际使用的话，如果你的WebView只是用来处理一些html的页面内容，只用WebViewClient就行了，
如果需要更丰富的处理效果，比如JS、进度条等，就要用到WebChromeClient。
更多的时候，你可以这样

WebView webView;
webView= (WebView) findViewById(R.id.webview);
webView.setWebChromeClient(new WebChromeClient());
webView.setWebViewClient(new WebViewClient());
webView.getSettings().setJavaScriptEnabled(true);
webView.loadUrl(url);
这样你的WebView理论上就能有大部分需要实现的特色了
*
*
* wView.loadUrl("file:///android_asset/index.html");
// 打开本地sd卡内的index.html文件
wView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
// 打开指定URL的html文件
wView.loadUrl("http://wap.baidu.com");

在WebView上加载html代码
String content = "<p><font color='red'>hello baidu!</font></p>";
webview.loadData(content, "text/html", "UTF-8"); // 加载定义的代码，并设定编码格式和字符

*
* */
public class WebViewAppLinkActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_webview);
        ButterKnife.bind(this);
        //loadWebView("file:///android_asset/hello.html");
        initWebView(mWebView);
        loadWebView("file:///android_asset/applink.html");
    }

    private void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(false);
//        webView.setWebViewClient(new WebViewClient(){
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
        webView.setWebChromeClient(new WebChromeClient());
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            settings.setTextZoom(100);
        } else {
            settings.setTextSize(WebSettings.TextSize.NORMAL);
        }
        settings.setUseWideViewPort(true);
        webView.setVerticalScrollBarEnabled(false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            settings.setLoadsImagesAutomatically(false);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            //https 与http 混合请求
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        try {//解决跨域问题
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = settings.getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);//利用反射机制去修改设置对象
                if (method != null) {
                    method.invoke(settings, true);//修改设置
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadWebView(String url) {
        mWebView.loadUrl(url);
    }




}
