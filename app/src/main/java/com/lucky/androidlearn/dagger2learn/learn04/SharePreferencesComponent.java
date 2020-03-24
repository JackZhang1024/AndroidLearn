package com.lucky.androidlearn.dagger2learn.learn04;


import dagger.Subcomponent;


/**
 * @author zfz
 * */
@Subcomponent(modules = SharePreferenceModule.class)
public interface SharePreferencesComponent {

    void inject(Dagger2Main5Activity activity);

    //void inject(Dagger2Main4Activity activity);

}
