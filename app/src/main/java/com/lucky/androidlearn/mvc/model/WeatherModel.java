package com.lucky.androidlearn.mvc.model;

import com.lucky.androidlearn.mvc.model.bean.WeatherModelBean;

/**
 * Created by zfz on 2018/2/4.
 */

public interface WeatherModel {

    // http://tj.nineton.cn/Heart/index/all?city=CHSH000000&language=zh-chs&unit=c&aqi=city&alarm=1
    // &key=78928e706123c1a8f1766f062bc8676b
    void getWeather(String cityID, OnRequestCallBack callBack);

    void postWeather(String cityID, OnRequestCallBack callBack);

}
