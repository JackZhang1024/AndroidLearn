package com.lucky.androidlearn.mvp.simplemvp.view;

public interface IDisplayView {

    void onTaskStart();

    void onSuccess();

    void onFail(Exception e);

}
