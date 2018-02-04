package com.lucky.androidlearn.widget.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.widget.common.editText.EditTextWidgetActivity;
import com.lucky.androidlearn.widget.common.fancygallery.FancyGalleryMainActivity;
import com.lucky.androidlearn.widget.common.helper.AlertDialogHelper;
import com.lucky.androidlearn.widget.common.pulllistview.PullListViewActivity;
import com.lucky.androidlearn.widget.constraintlayout.ConstraintLayoutActivity;
import com.lucky.androidlearn.widget.notification.NotificationActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/8/31.
 */

public class CommonWidgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_widget);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.seek_bar)
    public void onSeekBarClick(View view) {

    }

    @OnClick(R.id.btn_edittext)
    public void onEditTextClick(View view) {
        Intent intent = new Intent(this, EditTextWidgetActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_activity_dialog)
    public void onActivityDialogClick(View view) {
        Intent intent = new Intent(CommonWidgetActivity.this, ActivityDialogActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_dialog)
    public void onDialogClick(View view) {
        AlertDialogHelper dialogHelper = new AlertDialogHelper();
        dialogHelper.showAlert(this, "Old", "Old Content", new AlertDialogHelper.AlertDialogInterface() {
            @Override
            public void onPositiveButtonClick() {
                AppToastMgr.shortToast(CommonWidgetActivity.this, "Positive Click");
            }

            @Override
            public void onNegativeButtonClick() {
                AppToastMgr.shortToast(CommonWidgetActivity.this, "Negative Click");
            }
        });
    }

    @OnClick(R.id.btn_custom_dialog)
    public void onCustomDialogClick(View view) {


    }

    @OnClick(R.id.btn_bottom_popupwinodw)
    public void onBottomPopupWindowClick(View view) {


    }

    @OnClick(R.id.btn_top_popupwinodw)
    public void onTopPopupWindowClick(View view) {


    }

    @OnClick(R.id.btn_recycleview)
    public void onRecycleViewClick(View view) {
        startActivity(new Intent(this, RecyclerViewActivity.class));
    }

    @OnClick(R.id.btn_fancygallery)
    public void onFancyGalleryClick(View view) {
        startActivity(new Intent(this, FancyGalleryMainActivity.class));
    }

    @OnClick(R.id.btn_pulllistview)
    public void onPullListViewClick(View view) {
        startActivity(new Intent(this, PullListViewActivity.class));
    }

    @OnClick(R.id.btn_notification)
    public void onPushNotificationClick(View view) {
        startActivity(new Intent(this, NotificationActivity.class));
    }

    @OnClick(R.id.btn_constraint_layout)
    public void onConstraintLayoutClick(View view){
        startActivity(new Intent(this, ConstraintLayoutActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
