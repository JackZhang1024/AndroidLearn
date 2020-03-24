package com.lucky.androidlearn.dagger2learn.learn04;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.AndroidApplication;
import com.lucky.androidlearn.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zfz
 */
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
    }


}
