package com.lucky.androidlearn.mvp.baseimpl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jingewenku.abrahamcaijin.commonutil.AppManager;

/**
 * 一个简单的MVP框架
 * https://github.com/azhon/Mvp-RxJava-Retrofit
 * Created by zfz on 2018/2/4.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected P presenter;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        AppManager.getAppManager().addActivity(this);
        presenter = initPresenter();
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().removeActivity(this);
        if (presenter != null) {
            presenter.detach();
            presenter = null;
        }
        super.onDestroy();
    }

    protected abstract P initPresenter();

    @Override
    public void showLoadingDialog(String message) {

    }

    @Override
    public void dismissDialog() {

    }


}
