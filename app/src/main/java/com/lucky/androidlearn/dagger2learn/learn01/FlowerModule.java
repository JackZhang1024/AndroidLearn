package com.lucky.androidlearn.dagger2learn.learn01;


import com.lucky.androidlearn.dagger2learn.learn01.model.Flower;
import com.lucky.androidlearn.dagger2learn.learn01.model.Lily;
import com.lucky.androidlearn.dagger2learn.learn01.model.Rose;
import com.lucky.androidlearn.dagger2learn.learn01.qualifer.LilyFlower;
import com.lucky.androidlearn.dagger2learn.learn01.qualifer.RoseFlower;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class FlowerModule {

    @Named("rose")
    @Provides
    Flower provideRose(){
        return new Rose();
    }

    @Named("lily")
    @Provides
    Flower provideLily(){
        return new Lily();
    }

    @RoseFlower
    @Provides
    Flower provideQualiferRose(){
        return new Rose();
    }

    @LilyFlower
    @Provides
    Flower provdieQualifyLily(){
        return new Lily();
    }

}
