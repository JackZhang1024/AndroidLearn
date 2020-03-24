package com.lucky.androidlearn.mvp.retrofit;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.mvc.model.bean.WeatherItem;
import com.lucky.androidlearn.mvp.baseimpl.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * https://github.com/azhon/Mvp-RxJava-Retrofit
 *
 * Created by zfz on 2018/2/4.
 */

public class ComplexMVPActivity extends BaseActivity<TestContact.Presenter> implements TestContact.View{

    @BindView(R.id.tv_query_weather_result)
    TextView mTvWeatherQueryResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp_main2);
        ButterKnife.bind(this);
        presenter.getData();
    }

    @Override
    protected TestContact.Presenter initPresenter() {
        return new TestPresenter(this);
    }

    @Override
    public void setData(List<WeatherItem> weatherItemList) {
        StringBuffer stringBuffer = new StringBuffer();
        for (WeatherItem item: weatherItemList) {
            stringBuffer.append(item.getCity_name());
        }
        mTvWeatherQueryResult.setText(stringBuffer.toString());
    }

}
