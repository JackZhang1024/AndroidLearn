package com.lucky.androidlearn.dagger2learn.learn01.model;


import com.lucky.androidlearn.dagger2learn.learn01.qualifer.LilyFlower;
import com.lucky.androidlearn.dagger2learn.learn01.qualifer.RoseFlower;

import javax.inject.Inject;

public class ChainPot {

    private Flower mFlower;

    @Inject
    public ChainPot(@LilyFlower Flower flower) {
        this.mFlower = flower;
    }

    public String show(){
        return mFlower.whisper();
    }

}
