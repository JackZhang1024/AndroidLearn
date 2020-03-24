package com.lucky.androidlearn.mvp.simplemvp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.mvp.simplemvp.presenter.IPresenter;
import com.lucky.androidlearn.mvp.simplemvp.presenter.LoadNetWorkDataPresenter;
import com.lucky.androidlearn.mvp.simplemvp.view.LoadDataView;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * https://github.com/googlesamples/android-architecture
 *
 * @author zfz
 */
public class SimpleMVPActivity extends AppCompatActivity implements LoadDataView{
    private static final String TAG = "SimpleMVPActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_mvp);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_load_network_data)
    public void onLoadDataClick(){
        IPresenter presenter = new LoadNetWorkDataPresenter(this);
        presenter.loadNetWorkData("xx");
    }

    @Override
    public void onTaskStart() {

    }

    @Override
    public void displayResult(String result) {
        Log.e(TAG, "当前线程 "+Thread.currentThread().getName()+ "displayResult: "+result);
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail(Exception e) {

    }
}
