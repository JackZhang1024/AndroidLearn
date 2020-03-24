package com.lucky.androidlearn.loadsir;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.loadsir.loadcallback.EmptyCallback;
import com.lucky.androidlearn.loadsir.loadcallback.ErrorCallback;
import com.lucky.androidlearn.loadsir.loadcallback.LoadingCallback;

public class LoadSirFragment extends Fragment {

    private TextView mTvMsg;

    private LoadService mLoadSir;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new ErrorCallback())
                .setDefaultCallback(LoadingCallback.class)
                .build();
        View rootView = inflater.inflate(R.layout.fragment_loadsir, container, false);
        mLoadSir = loadSir.register(rootView, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {

            }
        });
        return mLoadSir.getLoadLayout();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvMsg = view.findViewById(R.id.tv_msg);
        loadDataFromNet();
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
