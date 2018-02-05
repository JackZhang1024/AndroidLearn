package com.lucky.androidlearn.dagger2learn.learn02.model;

public class Pot {

    private Flower mFlower;

    public Pot(Flower flower) {
        this.mFlower = flower;
    }

    public String show() {
        return mFlower.whisper();
    }

}
