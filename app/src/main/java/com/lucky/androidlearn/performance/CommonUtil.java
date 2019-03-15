package com.lucky.androidlearn.performance;

import android.content.Context;

public class CommonUtil {

    private static CommonUtil instance;
    private Context context;

    public CommonUtil(Context context){
         this.context = context;
    }

    public static CommonUtil getInstance(Context context){
        if (instance == null){
            instance = new CommonUtil(context);
        }
        return instance;
    }



}
