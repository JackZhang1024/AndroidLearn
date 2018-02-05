package com.lucky.androidlearn.dagger2learn.lesson04;

import android.content.Context;

import com.lucky.androidlearn.AndroidApplication;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

@Module
public class ToastManagerModule {

    @Provides
    ToastManager provideToastManager(Context application){
        return new ToastManager(application);
    }

}
