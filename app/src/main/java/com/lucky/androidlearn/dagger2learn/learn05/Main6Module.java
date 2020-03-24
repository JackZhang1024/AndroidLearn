package com.lucky.androidlearn.dagger2learn.learn05;

import com.lucky.androidlearn.dagger2learn.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class Main6Module {

    @ActivityScope
    @Provides
    Main6Data provideMain6Data(){
        return new Main6Data("Hello");
    }


}
