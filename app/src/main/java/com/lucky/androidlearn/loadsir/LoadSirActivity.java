package com.lucky.androidlearn.loadsir;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.loadsir.loadcallback.EmptyCallback;
import com.lucky.androidlearn.loadsir.loadcallback.ErrorCallback;
import com.lucky.androidlearn.loadsir.loadcallback.LoadingCallback;

// loadSir的使用学习
public class LoadSirActivity extends AppCompatActivity {

    private static final String TAG = "LoadSirActivity";

    private TextView mTvMsg;
    private LinearLayout mRoot;
    private LoadService mLoadSir;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadsir);
        mTvMsg = findViewById(R.id.tv_msg);
        mRoot = findViewById(R.id.root);
        mLoadSir = LoadSir.getDefault().register(mRoot, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                Log.e(TAG, "onReload: ");
            }
        });
//        loadDataFromNet();
        loadDataFromNetError();
//        loadDataFromNetEmpty();
    }

    private void loadDataFromNet() {
        mLoadSir.showCallback(LoadingCallback.class);
        new Handler().postDelayed(() -> {
            mLoadSir.showSuccess();
            mTvMsg.setText("返回成功了");
        }, 2000);
    }

    private void loadDataFromNetError() {
        mLoadSir.showCallback(LoadingCallback.class);
        new Handler().postDelayed(() -> {
            mLoadSir.setCallBack(ErrorCallback.class, new Transport() {
                @Override
                public void order(Context context, View view) {
                    // 设置错误页文字提示
                    ((TextView) view.findViewById(R.id.error_text)).setText("网络错误 404");
                }
            });
            mLoadSir.showCallback(ErrorCallback.class);
        }, 2000);
    }

    private void loadDataFromNetEmpty() {
        mLoadSir.showCallback(LoadingCallback.class);
        new Handler().postDelayed(() -> {
            mLoadSir.showCallback(EmptyCallback.class);
        }, 2000);
    }


}
