package com.lucky.androidlearn.dagger2learn.learn01.model;

import javax.inject.Inject;
import javax.inject.Named;

public class Pot {

    private Flower mFlower;

    @Inject
    public Pot(@Named("lily") Flower flower) {
        this.mFlower = flower;
    }

    public String show() {
        return mFlower.whisper();
    }

}
