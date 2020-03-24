package com.lucky.androidlearn.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.mvvm.learn.DataBindingRVActivity;
import com.lucky.androidlearn.mvvm.learn.MVVMLearnActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于MVVM的一些学习
 *
 * http://blog.csdn.net/zhouxu88/article/details/78284198
 * https://www.jianshu.com/p/2fc41a310f79
 * http://blog.csdn.net/u012702547/article/details/52077515
 *
 * MVVM框架学习使用
 * Created by zfz on 2018/2/2.
 *
 */

public class MVVMMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvvm_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_mvvm_learn)
    public void onMVVMLearnClick(View view){
        startActivity(new Intent(this, MVVMLearnActivity.class));
    }

    @OnClick(R.id.btn_mvvm_recycleview)
    public void onMVVMRecyclewClick(View view){
        startActivity(new Intent(this, DataBindingRVActivity.class));
    }

    @OnClick(R.id.btn_mvvm_practice)
    public void onMVVMPracticeClick(View view){
        startActivity(new Intent(this, DataBindingRVActivity.class));
    }

}
