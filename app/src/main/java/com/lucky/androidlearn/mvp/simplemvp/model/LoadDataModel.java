package com.lucky.androidlearn.mvp.simplemvp.model;

public interface LoadDataModel {

    void loadData(String param, LoadDataModelCallBack callBack);

    interface LoadDataModelCallBack{
        void onCallBack(String result);

        void onCallError(Exception e);
    }
}
