package com.lucky.androidlearn.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lucky.androidlearn.mvc.model.bean.WeatherItem;
import com.lucky.androidlearn.mvc.model.bean.WeatherModelBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zfz on 2018/2/2.
 */

public class MVPMainActivity extends AppCompatActivity {

    private static final String TAG = "MVPMain";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doSomething();
    }

    private void doSomething() {
        Map<String, String> params = new HashMap<>();
        RetrofitFactory.getInstance().getWeatherDetailInfo(params)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        //将这个请求的Disposable添加进入CompositeDisposable同一管理（在封装的presenter中）

                        //addDisposable(disposable);
                        //访问网络显示dialog
                        //view.showLoadingDialog("");
                    }
                })
                .map(new Function<WeatherModelBean, List<WeatherItem>>() {
                    @Override
                    public List<WeatherItem> apply(@NonNull WeatherModelBean weatherModelBean) throws Exception {
                        //转化数据
                        return weatherModelBean.getWeather();
                    }
                })
                //获得的数据返回主线程去更新界面
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<WeatherItem>>() {
                    @Override
                    public void accept(@NonNull List<WeatherItem>  weatherItems) throws Exception {
                        Log.e(TAG, "accept: ");
                        //消失dialog
                        //view.dismissLoadingDialog();
                        //设置数据
                        //view.setData(storiesBeen);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                        //view.dismissLoadingDialog();
                        //String exception = ExceptionHelper.handleException(throwable);
                        //打印出错误信息
                        //Log.e("TAG", "exception: " + exception);
                    }
                });

    }
}
