package com.lucky.androidlearn.dagger2learn.lesson04;


import android.app.Activity;

import dagger.Subcomponent;

@Subcomponent(modules = ToastManagerModule.class)
public interface ToastManagerComponent {

    void inject(Dagger2Main4Activity activity);

    //void inject(Dagger2Main5Activity activity);

}
