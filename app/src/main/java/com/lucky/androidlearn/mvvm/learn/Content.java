package com.lucky.androidlearn.mvvm.learn;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lucky.androidlearn.BR;

/**
 * Created by zfz on 2018/2/5.
 */

public class Content extends BaseObservable{

    private String title;
    private String subTitle;

    public Content(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        notifyPropertyChanged(BR._all);
    }

}
