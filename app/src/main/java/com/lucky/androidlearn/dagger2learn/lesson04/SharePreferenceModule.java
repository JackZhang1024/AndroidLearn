package com.lucky.androidlearn.dagger2learn.lesson04;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class SharePreferenceModule {

    @Provides
    SharePreferenceManager provideSharePreferenceManager(Context context){
        return new SharePreferenceManager(context);
    }

}
