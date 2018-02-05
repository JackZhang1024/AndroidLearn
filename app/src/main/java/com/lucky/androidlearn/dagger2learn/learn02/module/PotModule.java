package com.lucky.androidlearn.dagger2learn.learn02.module;


import com.lucky.androidlearn.dagger2learn.learn02.model.Flower;
import com.lucky.androidlearn.dagger2learn.learn02.model.Pot;
import com.lucky.androidlearn.dagger2learn.learn02.qualifier.LilyQualifer;
import com.lucky.androidlearn.dagger2learn.learn02.qualifier.RoseQualifer;

import dagger.Module;
import dagger.Provides;

@Module
public class PotModule {

    /**
     * PotModule需要依赖Flower，需要指定其中一个子类实现，这里使用RoseFlower
     * */
    @Provides
    Pot providePot(@RoseQualifer Flower flower){
        return new Pot(flower);
    }

}
