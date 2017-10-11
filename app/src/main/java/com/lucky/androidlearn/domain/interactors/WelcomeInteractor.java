package com.lucky.androidlearn.domain.interactors;


import com.lucky.androidlearn.domain.interactors.base.Interactor;

/**
 * Created by zfz on 2017/1/1.
 */

public interface WelcomeInteractor extends Interactor {

    //回调处理
    //在主线程中负责处理工作线程返回的数据
    interface Callback{
        void onMessageRetrieved(String message);
        void onRetrieveError(String error);
    }

}
