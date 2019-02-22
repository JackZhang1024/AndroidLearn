package com.lucky.androidlearn.hybrid;

import android.webkit.JavascriptInterface;

public class InnerJavaModel {


    @JavascriptInterface
    public void sayHello(String name) {
        System.out.println(name+" sayHello ");
    }



}
