package com.lucky.androidlearn.mvp.retrofit;

import com.lucky.androidlearn.mvc.model.bean.WeatherItem;
import com.lucky.androidlearn.mvp.baseimpl.BasePresenter;
import com.lucky.androidlearn.mvp.baseimpl.BaseView;

import java.util.List;

/**
 * 接口管理
 * Created by zfz on 2018/2/4.
 */

public interface TestContact {

    interface Presenter extends BasePresenter{
        void getData();

        void getIPInfo();
    }

    interface View extends BaseView{
        void setData(List<WeatherItem> weatherItemList);


    }

}
