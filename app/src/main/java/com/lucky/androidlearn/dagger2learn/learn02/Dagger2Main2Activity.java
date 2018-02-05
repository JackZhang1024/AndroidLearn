package com.lucky.androidlearn.dagger2learn.learn02;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lucky.androidlearn.AndroidApplication;
import com.lucky.androidlearn.dagger2learn.learn02.component.DaggerDagger2Main2Component;
import com.lucky.androidlearn.dagger2learn.learn02.component.DaggerFlowerComponent;
import com.lucky.androidlearn.dagger2learn.learn02.component.DaggerPotComponent;
import com.lucky.androidlearn.dagger2learn.learn02.model.Pot;

import javax.inject.Inject;

/**
 * Created by zfz on 2017/2/9.
 */

public class Dagger2Main2Activity extends AppCompatActivity {
    private static final String TAG = "Dagger2Main2Activity";

    @Inject
    Pot mPot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerDagger2Main2Component.builder().potComponent(
                DaggerPotComponent.builder().flowerComponent(
                        DaggerFlowerComponent.create()
                ).build()

        ).build().inject(this);
        Log.e(TAG, "onCreate: "+mPot.show());
    }
}


