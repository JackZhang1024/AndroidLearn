package com.lucky.androidlearn.permission;

import android.Manifest;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jingewenku.abrahamcaijin.commonutil.AppLogMessageMgr;
import com.jingewenku.abrahamcaijin.commonutil.AppPhoneMgr;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.widget.common.helper.AlertDialogHelper;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Created by zfz on 2017/12/24.
 */

public class PermissionActivity extends AppCompatActivity {
    private static final String TAG = "PermissionActivity";
    private static final String notice =
            "在API>=23（Android6.0之后）我们需要动态的处理权限问题 而在API<23之前（Android6.0之前）我们只需要在Manifest文件中 " +
                    "声明我们需要的权限就可以处理" +
                    "因为在Install的时候就已经默认授权";

    @BindView(R.id.tv_permission_manage)
    TextView tvPermissionManage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        ButterKnife.bind(this);
        tvPermissionManage.setText(notice);
        requestPermissions();
    }

    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS)
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
        dialogHelper.showAlert(this, "提示", message, new AlertDialogHelper.AlertDialogInterface() {
            @Override
            public void onPositiveButtonClick() {
                AppPhoneMgr.toAppSettingsApp(PermissionActivity.this);
            }

            @Override
            public void onNegativeButtonClick() {

            }
        });
    }




}
