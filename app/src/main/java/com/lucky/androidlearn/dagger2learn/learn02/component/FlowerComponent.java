package com.lucky.androidlearn.dagger2learn.learn02.component;

import com.lucky.androidlearn.dagger2learn.learn02.module.FlowerModule;
import com.lucky.androidlearn.dagger2learn.learn02.model.Flower;
import com.lucky.androidlearn.dagger2learn.learn02.qualifier.LilyQualifer;
import com.lucky.androidlearn.dagger2learn.learn02.qualifier.RoseQualifer;

import dagger.Component;

@Component(modules = FlowerModule.class)
public interface FlowerComponent {

    @RoseQualifer
    Flower getRoseFlower();

    @LilyQualifer
    Flower getLilyFlower();
}
