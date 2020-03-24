package com.lucky.androidlearn.dagger2learn.learn01;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.dagger2learn.learn01.model.ChainPot;
import com.lucky.androidlearn.dagger2learn.learn01.model.Pot;

import javax.inject.Inject;

/**
 * https://www.jianshu.com/p/24af4c102f62 入门教程
 * https://github.com/luxiaoming/dagger2Demo#begin
 * <p>
 * Component的三种用法
 *
 * @author zfz
 */
public class Dagger2MainActivity extends AppCompatActivity {
    private static final String TAG = "Dagger2MainActivity";

    @Inject
    Pot pot;

    @Inject
    ChainPot chainPot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerDagger2MainComponent.create().inject(this);
        Log.e(TAG, "onCreate: "+pot.show());
        Log.e(TAG, "onCreate: "+chainPot.show());
    }
}
