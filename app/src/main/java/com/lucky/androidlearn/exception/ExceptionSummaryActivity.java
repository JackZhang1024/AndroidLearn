package com.lucky.androidlearn.exception;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.exception.oom.OOMActivity;
import com.lucky.androidlearn.exception.toast.ToastExceptionActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * 异常总结
 */
public class ExceptionSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception_summary);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_toast_exception)
    public void onToastException(View view) {
        Intent intent = new Intent(this, ToastExceptionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_oom_creator)
    public void onOOMCreatorException(View view) {
        Intent intent = new Intent(this, OOMActivity.class);
        startActivity(intent);
    }
}
