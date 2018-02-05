package com.lucky.androidlearn.dagger2learn.learn03.component;

import com.lucky.androidlearn.dagger2learn.learn03.module.ClassModule;
import com.lucky.androidlearn.dagger2learn.learn03.module.SchoolModule;

import dagger.Component;
import dagger.Subcomponent;

@Subcomponent(modules = ClassModule.class)
public interface Dagger2Main3ClassComponent {

    Dagger2Main3SchoolComponent plus(SchoolModule schoolModule);

}
