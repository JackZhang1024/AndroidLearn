package com.lucky.androidlearn.mvp.simplemvp.presenter;

import com.lucky.androidlearn.mvp.simplemvp.model.LoadDataModel;
import com.lucky.androidlearn.mvp.simplemvp.model.LoadDataModelImpl;
import com.lucky.androidlearn.mvp.simplemvp.view.IDisplayView;
import com.lucky.androidlearn.mvp.simplemvp.view.LoadDataView;


/**
 * @author zfz
 * */
public class LoadNetWorkDataPresenter implements IPresenter {

    private LoadDataView displayView;

    public LoadNetWorkDataPresenter(LoadDataView displayView) {
        this.displayView = displayView;
    }

    @Override
    public void loadNetWorkData(String param) {
        // loadNetWorkData and display Data
        LoadDataModel loadDataModel = new LoadDataModelImpl();
        loadDataModel.loadData("hello", new LoadDataModel.LoadDataModelCallBack() {
            @Override
            public void onCallBack(String result) {
                displayView.displayResult(result);
            }

            @Override
            public void onCallError(Exception e) {
                displayView.onFail(e);
            }
        });

    }
}
