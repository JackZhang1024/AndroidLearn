package com.lucky.androidlearn.dagger2learn.lesson04;


import android.app.Activity;

import dagger.Subcomponent;


/**
 * @author zfz
 * */
@Subcomponent(modules = SharePreferenceModule.class)
public interface SharePreferencesComponent {

    void inject(Dagger2Main5Activity activity);

    //void inject(Dagger2Main4Activity activity);

}
