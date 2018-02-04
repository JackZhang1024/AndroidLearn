package com.lucky.androidlearn.mvp.retrofit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.mvc.model.bean.WeatherItem;
import com.lucky.androidlearn.mvp.baseimpl.BaseActivity;
import com.orhanobut.logger.Logger;
import org.w3c.dom.Text;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zfz on 2018/2/4.
 */

public class MVPMain2Activity extends BaseActivity<TestContact.Presenter> implements TestContact.View{

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
