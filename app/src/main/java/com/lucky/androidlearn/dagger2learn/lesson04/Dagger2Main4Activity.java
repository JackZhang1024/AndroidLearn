package com.lucky.androidlearn.dagger2learn.lesson04;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lucky.androidlearn.AndroidApplication;
import com.lucky.androidlearn.R;
import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zfz
 * */
public class Dagger2Main4Activity extends AppCompatActivity {

    @Inject
    ToastManager mToastManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daager2_main_4);
        ButterKnife.bind(this);
        AndroidApplication.getInstances().getAppComponent().plusToastManagerComponent(new ToastManagerModule()).inject(this);

        //AndroidApplication.getInstances().getAppComponent().plusSharePreferencesComponent(new SharePreferenceModule()).inject(this);
        //mSharePreferencesManager.commit();

    }

    @OnClick(R.id.btn_click_toast)
    public void onClickToast() {
        mToastManager.showToast("Hello World");
        startActivity(new Intent(this, Dagger2Main5Activity.class));
    }


}
