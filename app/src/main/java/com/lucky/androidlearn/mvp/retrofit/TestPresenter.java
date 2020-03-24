package com.lucky.androidlearn.mvp.retrofit;

import androidx.annotation.NonNull;

import com.lucky.androidlearn.mvc.model.bean.WeatherItem;
import com.lucky.androidlearn.mvc.model.bean.WeatherModelBean;
import com.lucky.androidlearn.mvp.RetrofitExceptionHandler;
import com.lucky.androidlearn.mvp.RetrofitFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zfz on 2018/2/4.
 */

public class TestPresenter extends BasePresenterImpl<TestContact.View> implements TestContact.Presenter {

    public TestPresenter(TestContact.View baseView) {
        super(baseView);
    }

    @Override
    public void getData() {
        Map<String, String> params = new HashMap<>();
        params.put("city", "CHSH000000");
        RetrofitFactory.getInstance()
                .getWeatherDetailInfo(params) //测试接口
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        addDisposable(disposable);//请求加入管理
                        mBaseView.showLoadingDialog("开始请求数据...");
                    }
                })
                .map(new Function<WeatherModelBean, List<WeatherItem>>() {
                    @Override
                    public List<WeatherItem> apply(@NonNull WeatherModelBean weatherModelBean) throws Exception {
                        return weatherModelBean.getWeather();//转换数据
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<WeatherItem>>() {
                    @Override
                    public void accept(@NonNull List<WeatherItem> weatherItemList) throws Exception {
                        mBaseView.dismissDialog();
                        mBaseView.setData(weatherItemList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mBaseView.dismissDialog();
                        RetrofitExceptionHandler.handleException(throwable);
                    }
                });
    }

}
