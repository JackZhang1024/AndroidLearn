package com.lucky.androidlearn.hybrid;

import android.webkit.JavascriptInterface;

public class InnerJavaModel {

    public String name;
    public int age;

    public InnerJavaModel(){
        name = "Jack";
        age = 24;
    }

    @JavascriptInterface
    public void sayHello(String name) {
        System.out.println(name+" sayHello ");
    }


    @JavascriptInterface
    public int getAge() {
        return age;
    }

    @JavascriptInterface
    public String getName() {
        return name;
    }
}
