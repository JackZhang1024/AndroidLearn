package com.lucky.androidlearn.permission;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.lucky.androidlearn.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/12/24.
 */

public class PermissionManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_manage);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_activity_permission)
    public void onActivityPermissionClick(View view) {
        Intent intent = new Intent(this, PermissionActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_fragment_permission)
    public void onFragmentPermissionClick(View view) {
        Intent intent = new Intent(this, PermissionFragmentActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_location_permission)
    public void onLocationPermissionClick(View view) {
        Intent intent = new Intent(this, LocationPermissionCheckActivity.class);
        startActivity(intent);
    }


}
