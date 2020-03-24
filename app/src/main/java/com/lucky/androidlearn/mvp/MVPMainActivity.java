package com.lucky.androidlearn.mvp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.mvp.retrofit.ComplexMVPActivity;
import com.lucky.androidlearn.mvp.simplemvp.SimpleMVPActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2018/2/2.
 */

public class MVPMainActivity extends AppCompatActivity {

    private static final String TAG = "MVPMain";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp_main);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_simple_mvp)
    public void onSimpleMVPClick() {
        Intent intent = new Intent(this, SimpleMVPActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.btn_complex_mvp)
    public void onComplexMVPClick() {
        Intent intent = new Intent(this, ComplexMVPActivity.class);
        startActivity(intent);
    }


}
