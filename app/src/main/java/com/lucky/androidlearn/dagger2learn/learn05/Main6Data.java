package com.lucky.androidlearn.dagger2learn.learn05;


import android.util.Log;

public class Main6Data {
    private static final String TAG = "Main6Data";

    private String name;

    public Main6Data(String name){
        this.name = name;
    }


    public void showName(){
        Log.d(TAG, "showName: "+name);
    }

}
