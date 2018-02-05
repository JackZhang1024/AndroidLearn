package com.lucky.androidlearn.dagger2learn;

import android.app.Service;
import android.content.Context;
import android.location.LocationManager;

import com.lucky.androidlearn.AndroidApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zfz on 2017/2/9.
 * 用于提供各个模块中所需要的实例对象
 */

@Singleton
@Module
public class AppModule {
    private AndroidApplication myApplication;

    public AppModule(){

    }

    public AppModule(AndroidApplication application){
        myApplication=application;
    }

    @Singleton
    @Provides
    Context provideContext(){
        return myApplication;
    }

    @Singleton
    @Provides
    LocationManager provideLocationManager(){
        return (LocationManager) myApplication.getSystemService(Service.LOCATION_SERVICE);
    }

}
