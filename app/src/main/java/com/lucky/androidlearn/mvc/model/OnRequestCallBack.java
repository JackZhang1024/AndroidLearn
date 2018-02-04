package com.lucky.androidlearn.mvc.model;

import com.lucky.androidlearn.mvc.model.bean.WeatherModelBean;

/**
 * Created by zfz on 2018/2/4.
 */

public interface OnRequestCallBack {

    void onRequestSuccess(String message);

    void onRequestFail(String error);

    void onRequestSuccess(WeatherModelBean weatherModelBean);
}
