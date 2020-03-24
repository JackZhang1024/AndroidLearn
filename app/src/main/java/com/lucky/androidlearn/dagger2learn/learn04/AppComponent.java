package com.lucky.androidlearn.dagger2learn.learn04;

import android.location.LocationManager;

import com.lucky.androidlearn.AndroidApplication;
import com.lucky.androidlearn.dagger2learn.AppModule;
import com.lucky.androidlearn.dagger2learn.GlobalModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author zfz
 * Created by zfz on 2017/2/9.
 */
@Singleton
@Component(modules = {AppModule.class, GlobalModule.class})
public interface AppComponent {

    /**
     * inject后面的是我们要将AppModule类中提供的数据注入的地方
     * 比如将AppModule中提供的Person实例注入到MainActivity中
     * void inject(Dagger2MainActivity mainActivity);
     * @param mainActivity
     */

    /**
     * 注入到AndroidApplication
     *
     * @param myApplication
     */
    void inject(AndroidApplication myApplication);

    /**
     * 这块又涉及到了另外一个知识点：组件之间的依赖关系
     * 什么称之为组件之间的依赖 就是一个组件中需要的某个实例会由另外一个组件中的module来提供
     *
     * @return
     */
    LocationManager getLocationManager();

    /**
     * 拓展出新的ToastManager功能
     */
    ToastManagerComponent plusToastManagerComponent(ToastManagerModule module);

    /**
     * 拓展出新的SharePreference功能
     */
    SharePreferencesComponent plusSharePreferencesComponent(SharePreferenceModule module);



}
