package com.lucky.androidlearn.mvp.simplemvp.model;

import com.lucky.androidlearn.domain.executor.Executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadDataModelImpl implements LoadDataModel {

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    public LoadDataModelImpl(){

    }

    @Override
    public void loadData(String params, LoadDataModelCallBack callBack) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3*1000);
                    callBack.onCallBack("我们都是好孩子....");
                }catch (Exception e){
                    callBack.onCallError(e);
                }
            }
        });
    }
}
