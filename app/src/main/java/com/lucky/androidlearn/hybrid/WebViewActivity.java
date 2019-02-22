package com.lucky.androidlearn.hybrid;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    InnerWebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        loadWebView("file:///android_asset/hello.html");
        InnerJavaModel model = new InnerJavaModel();
        mWebView.setJavaScript("WebAppInterface", model);
    }

    private void loadWebView(String url) {
        mWebView.loadUrl(url);
    }


    private void executeJavaScript() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String jsFunction = "function changeName(name){model.name=name; alert(model.name)}; changeName('Han')";
                mWebView.loadUrl("javascript:" + jsFunction);
            }
        }, 1000);
    }


}
