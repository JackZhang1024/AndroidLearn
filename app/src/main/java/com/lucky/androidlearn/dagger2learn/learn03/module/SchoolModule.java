package com.lucky.androidlearn.dagger2learn.learn03.module;

import com.lucky.androidlearn.dagger2learn.learn03.model.Klazz;
import com.lucky.androidlearn.dagger2learn.learn03.model.School;

import dagger.Module;
import dagger.Provides;

@Module
public class SchoolModule {

    @Provides
    School provideSchool(Klazz klzz){
        return new School(klzz);
    }

}
