package com.lucky.androidlearn.rxjava2.task;

import java.util.List;

public interface SearchCallBack {

    void onCallBack(List<String> results, boolean abortDisplay);
}
