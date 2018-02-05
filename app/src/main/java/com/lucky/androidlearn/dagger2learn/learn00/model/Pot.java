package com.lucky.androidlearn.dagger2learn.learn00.model;

import javax.inject.Inject;

public class Pot {

    private Rose mRose;

    @Inject
    public Pot(Rose rose) {
        this.mRose = rose;
    }

    // 注意 Inject只能标记一个构造器
//    @Inject
//    public Pot(){
//
//    }

    public String show() {
        return mRose.whisper();
    }

}
