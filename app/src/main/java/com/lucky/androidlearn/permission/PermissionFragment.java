package com.lucky.androidlearn.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jingewenku.abrahamcaijin.commonutil.AppLogMessageMgr;
import com.jingewenku.abrahamcaijin.commonutil.AppPhoneMgr;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.widget.common.helper.AlertDialogHelper;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tbruyelle.rxpermissions2.RxPermissionsFragment;
import io.reactivex.functions.Consumer;

/**
 * Created by zfz on 2017/12/24.
 */

public class PermissionFragment extends Fragment {
    private static final String TAG = "PermissionFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_permission, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestPermission();
    }

    private void requestPermission() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.requestEach(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CALENDAR)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            AppLogMessageMgr.e(TAG, "授权了权限 " + permission.name);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            AppLogMessageMgr.e(TAG, "点击了拒绝但是没有点击不再询问 " + permission.name);
                        } else {
                            AppLogMessageMgr.e(TAG, "点击了拒绝同时点击不再询问 " + permission.name);
                            showAlertDialog("需要打开权限  " + permission.name);
                        }
                    }
                });
    }

    private void showAlertDialog(String message) {
        AlertDialogHelper dialogHelper = new AlertDialogHelper();
        dialogHelper.showAlert(getActivity(), "提示", message, new AlertDialogHelper.AlertDialogInterface() {
            @Override
            public void onPositiveButtonClick() {
                AppPhoneMgr.toAppSettingsApp(getActivity());
            }

            @Override
            public void onNegativeButtonClick() {

            }
        });
    }

}
