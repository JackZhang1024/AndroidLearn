package com.lucky.androidlearn.dagger2learn.lesson04;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.lucky.androidlearn.AndroidApplication;
import javax.inject.Inject;


/**
 * @author zfz
 * */
public class Dagger2Main5Activity extends AppCompatActivity {

    @Inject
    SharePreferenceManager mSharePreferencesManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplication.getInstances().getAppComponent().plusSharePreferencesComponent(new SharePreferenceModule()).inject(this);
        mSharePreferencesManager.commit();
    }
}
