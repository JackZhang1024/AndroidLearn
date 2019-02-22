package com.lucky.androidlearn.hybrid;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.Method;

public class InnerWebView extends WebView {

    public InnerWebView(Context context) {
        this(context, null);
    }

    public InnerWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    @android.webkit.JavascriptInterface
    public void setJavaScript(String name, InnerJavaModel model) {
        addJavascriptInterface(model, name);
    }

    public void loadJSData(String jsData) {
        loadUrl(jsData);
    }


    private void initWebView() {
        WebSettings settings = getSettings();
        settings.setBuiltInZoomControls(false);
        setWebViewClient(new InnerWebViewClient());
        setWebChromeClient(new InnerWebChromeClient());
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
        setVerticalScrollBarEnabled(false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            settings.setLoadsImagesAutomatically(false);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            //https 与http 混合请求
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
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


}
