package com.lucky.androidlearn.dagger2learn.learn04;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ToastManagerModule {

    @Provides
    ToastManager provideToastManager(Context application){
        return new ToastManager(application);
    }

}
