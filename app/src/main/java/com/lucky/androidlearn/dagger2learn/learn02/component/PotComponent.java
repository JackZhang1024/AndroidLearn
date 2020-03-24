package com.lucky.androidlearn.dagger2learn.learn02.component;


import com.lucky.androidlearn.dagger2learn.learn02.module.PotModule;
import com.lucky.androidlearn.dagger2learn.learn02.model.Pot;

import dagger.Component;

@Component(dependencies = {FlowerComponent.class}, modules = {PotModule.class})
public interface PotComponent {

    Pot getPot();

}
