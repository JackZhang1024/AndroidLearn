package com.lucky.androidlearn.rxjava;

import android.util.Log;

/**
 * @author zfz
 * Created by zfz on 2018/3/13.
 */

public class GirlObserver implements Observer {
    private static final String TAG = "GirlObserver";
    private String name;

    public GirlObserver(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(String state) {
        Log.e(TAG, getName()+ ": "+state);
    }
}
