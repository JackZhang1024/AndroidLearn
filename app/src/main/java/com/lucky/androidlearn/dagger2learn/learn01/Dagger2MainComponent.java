package com.lucky.androidlearn.dagger2learn.learn01;


import dagger.Component;

@Component(modules = {FlowerModule.class, ChainPotModule.class})
public interface Dagger2MainComponent {

    void inject(Dagger2MainActivity activity);
}
