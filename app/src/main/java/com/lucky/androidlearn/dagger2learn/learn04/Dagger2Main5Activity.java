package com.lucky.androidlearn.dagger2learn.learn04;

import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.AndroidApplication;

import javax.inject.Inject;


/**
 * @author zfz
 * */
public class Dagger2Main5Activity extends AppCompatActivity {

    @Inject
    SharePreferenceManager mSharePreferencesManager;

    @Inject
    LocationManager mLocationManager1;

    @Inject
    LocationManager mLocationManager2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplication.getInstances().getAppComponent().plusSharePreferencesComponent(new SharePreferenceModule()).inject(this);
        mSharePreferencesManager.commit();
        System.out.println("locationManager==null"+(mLocationManager1==null));
        System.out.println("locationManager1==locationManager2"+(mLocationManager1==mLocationManager2));
    }
}
