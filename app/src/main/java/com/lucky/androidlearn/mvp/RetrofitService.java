package com.lucky.androidlearn.mvp;

import com.lucky.androidlearn.mvc.model.bean.WeatherModelBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by zfz on 2018/2/4.
 */

public interface RetrofitService {

    String BASE_URL = "http://tj.nineton.cn/";

    @GET("Heart/index/all")
    Observable<WeatherModelBean> getWeatherDetailInfo(@QueryMap Map<String, String> params);

}
