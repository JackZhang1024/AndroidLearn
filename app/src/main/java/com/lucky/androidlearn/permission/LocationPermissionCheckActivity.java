package com.lucky.androidlearn.permission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;

import com.jingewenku.abrahamcaijin.commonutil.AppPhoneMgr;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.widget.common.helper.AlertDialogHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 定位权限检查
 * Created by zfz on 2018/1/24.
 */

public class LocationPermissionCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_permission);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_check_permission)
    public void checkLocationPermission() {
        if (!checkLocationPermissionSettled()) {
            showAlertDialog("需要打开定位权限");
        }else{
            AppToastMgr.Toast(LocationPermissionCheckActivity.this, "定位权限已经打开");
        }
    }

    private void showAlertDialog(String message) {
        AlertDialogHelper dialogHelper = new AlertDialogHelper();
        dialogHelper.showAlert(this, "提示", message, new AlertDialogHelper.AlertDialogInterface() {
            @Override
            public void onPositiveButtonClick() {
                AppPhoneMgr.toAppSettingsApp(LocationPermissionCheckActivity.this);
            }

            @Override
            public void onNegativeButtonClick() {

            }
        });
    }

    private boolean checkLocationPermissionSettled() {
        if (Build.VERSION.SDK_INT >= 23) {
            return checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    checkPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            return checkPermissionGrantedUnder23(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    checkPermissionGrantedUnder23(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    private boolean checkPermissionGranted(String permission) {
        return this.checkPermission(permission, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkPermissionGrantedUnder23(String permission) {
        return PermissionChecker.checkPermission(this, permission, Process.myPid(), Process.myUid(), getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }

}
