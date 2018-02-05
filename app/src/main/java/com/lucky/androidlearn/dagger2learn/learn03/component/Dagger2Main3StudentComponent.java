package com.lucky.androidlearn.dagger2learn.learn03.component;


import com.lucky.androidlearn.dagger2learn.learn03.module.ClassModule;
import com.lucky.androidlearn.dagger2learn.learn03.module.StudentModule;

import dagger.Component;

@Component(modules = StudentModule.class)
public interface Dagger2Main3StudentComponent {

    Dagger2Main3ClassComponent plus(ClassModule classModule);

}
