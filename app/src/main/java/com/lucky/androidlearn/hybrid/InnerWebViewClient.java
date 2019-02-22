package com.lucky.androidlearn.hybrid;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jingewenku.abrahamcaijin.commonutil.klog.KLog;
import com.orhanobut.logger.Logger;

public class InnerWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        return super.shouldOverrideUrlLoading(view, url);
       view.loadUrl(url);
       return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        KLog.e("onPageStarted...");
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        KLog.e("onPageFinished...");
        // 在页面载入完成之后 执行JavaScript代码
        String jsFunction = "function changeName(name){model.name=name; alert(model.name)}; changeName('Han')";
        view.loadUrl("javascript:" + jsFunction);
    }

}
