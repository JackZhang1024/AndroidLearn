package com.lucky.androidlearn.dagger2learn.learn01;

import com.lucky.androidlearn.dagger2learn.learn01.model.ChainPot;
import com.lucky.androidlearn.dagger2learn.learn01.model.Flower;
import com.lucky.androidlearn.dagger2learn.learn01.model.Pot;
import com.lucky.androidlearn.dagger2learn.learn01.qualifer.LilyFlower;

import dagger.Module;
import dagger.Provides;

@Module
public class ChainPotModule {

    @Provides
    ChainPot provideChainPot(@LilyFlower Flower flower){
        return new ChainPot(flower);
    }

}
