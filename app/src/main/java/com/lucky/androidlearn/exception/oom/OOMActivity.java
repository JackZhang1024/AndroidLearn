package com.lucky.androidlearn.exception.oom;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

//https://www.jianshu.com/p/2fdee831ed03
public class OOMActivity extends AppCompatActivity {

    private static final String TAG = "OOMActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oom);
        ButterKnife.bind(this);
    }


    //btn_oom_creator
    @OnClick(R.id.btn_oom_creator)
    public void onOOMCreatorException(View view) {
//        String result = "";
//        for (int index = 0; index < 100000; index++) {
//            String temp = new String("123456");
//            result += temp;
//        }
        try {
            byte[] array = new byte[1024 * 1024 * 2000];
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "onOOMCreatorException: 发生OOM了啊");
        }

    }


}
