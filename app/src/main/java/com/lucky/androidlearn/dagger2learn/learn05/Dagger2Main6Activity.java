package com.lucky.androidlearn.dagger2learn.learn05;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

public class Dagger2Main6Activity extends AppCompatActivity {

    private static final String TAG = "Dagger2Main6Activity";

    // @ActivityScope
    // @FragmentScope

    @Inject
    Main6Data main6Data;

    Main6Component mDaggerMain6Component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DaggerMain6Component.builder().build().inject(this);
        mDaggerMain6Component = DaggerMain6Component.create();
        DaggerMain6Component.create().inject(this);
        main6Data.showName();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDaggerMain6Component != null) {
            mDaggerMain6Component = null;
        }
    }


}
