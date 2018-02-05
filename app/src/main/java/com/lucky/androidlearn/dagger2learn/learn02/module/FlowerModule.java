package com.lucky.androidlearn.dagger2learn.learn02.module;

import com.lucky.androidlearn.dagger2learn.learn02.model.Flower;
import com.lucky.androidlearn.dagger2learn.learn02.model.Lily;
import com.lucky.androidlearn.dagger2learn.learn02.model.Rose;
import com.lucky.androidlearn.dagger2learn.learn02.qualifier.LilyQualifer;
import com.lucky.androidlearn.dagger2learn.learn02.qualifier.RoseQualifer;

import dagger.Module;
import dagger.Provides;

@Module
public class FlowerModule {

    @RoseQualifer
    @Provides
    Flower provideRose() {
        return new Rose();
    }

    @LilyQualifer
    @Provides
    Flower provideLily() {
        return new Lily();
    }

}
