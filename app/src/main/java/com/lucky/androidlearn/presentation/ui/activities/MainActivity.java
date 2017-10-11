package com.lucky.androidlearn.presentation.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.domain.executor.impl.ThreadExecutor;
import com.lucky.androidlearn.presentation.presenters.MainPresenter;
import com.lucky.androidlearn.presentation.presenters.impl.MainPresenterImpl;
import com.lucky.androidlearn.storage.WelcomeMessageRepository;
import com.lucky.androidlearn.threading.MainThreadImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * MVP设计模式:
 * 1.设计一个Presenter接口
 * 2.在Presenter接口中再写一个View接口
 * 3.实现Presenter接口 PresenterImpl 这个实现的构造方法中有View
 * 4.在Activity实现View接口
 * 5.在Activity中设定一个Presenter接口对象
 * 6.触发事件用Presenter来调用其中定义好的方法
 * 7.触发事件处理结果用View的接口回调来处理用户的交互结果
 *
 * 总结：简洁、高效
 *
 * */
public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter=new MainPresenterImpl(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(),
                this,new WelcomeMessageRepository());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainPresenter.resume();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mMainPresenter.destroy();
    }
}
