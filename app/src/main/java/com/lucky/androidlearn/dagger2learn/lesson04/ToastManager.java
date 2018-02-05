package com.lucky.androidlearn.dagger2learn.lesson04;

import android.content.Context;
import android.widget.Toast;

import com.lucky.androidlearn.AndroidApplication;

public class ToastManager {

    private Context mApplication;

    public ToastManager(Context application) {
        this.mApplication = application;
    }

    public void showToast(String message){
        Toast.makeText(mApplication, message, Toast.LENGTH_LONG).show();
    }


}
