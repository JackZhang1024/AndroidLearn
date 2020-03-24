package com.lucky.androidlearn.exception.toast;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Toast异常
 */
public class ToastExceptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_exception);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_toast)
    public void showToastException(View view) {
        try {
            //执行报错 BadTokenException
            //Toast.makeText(this, "Hello World!", Toast.LENGTH_SHORT).show();

            ToastUtil toastUtil = new ToastUtil(this);
            toastUtil.info("Hello World!");
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
