package com.lucky.androidlearn.presentation.presenters;


import com.lucky.androidlearn.presentation.presenters.base.BasePresenter;
import com.lucky.androidlearn.presentation.ui.BaseView;

public interface MainPresenter extends BasePresenter {

    //BaseView与View的作用就是在交互的界面上进行相应的结果显示
    interface View extends BaseView {
        // TODO: Add your view methods
        void displayMessage(String message);
    }

    // TODO: Add your presenter methods
}
