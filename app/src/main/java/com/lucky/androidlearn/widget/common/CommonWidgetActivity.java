package com.lucky.androidlearn.widget.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.widget.bottomlayout.BottomLayoutActivity;
import com.lucky.androidlearn.widget.common.editText.EditTextWidgetActivity;
import com.lucky.androidlearn.widget.common.fancygallery.FancyGalleryMainActivity;
import com.lucky.androidlearn.widget.common.helper.AlertDialogHelper;
import com.lucky.androidlearn.widget.common.pulllistview.PullListViewActivity;
import com.lucky.androidlearn.widget.common.scrollconflict.ScrollConflictActivity;
import com.lucky.androidlearn.widget.common.scrollconflict.ScrollConflictDemo1Activity;
import com.lucky.androidlearn.widget.common.scrollconflict.ScrollConflictDemo2Activity;
import com.lucky.androidlearn.widget.common.scrollconflict.WaterFallActivity;
import com.lucky.androidlearn.widget.constraintlayout.ConstraintLayoutActivity;
import com.lucky.androidlearn.widget.marquee.MarqueeViewActivity;
import com.lucky.androidlearn.widget.notification.NotificationActivity;
import com.lucky.androidlearn.yoga.YogaLayoutActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/8/31.
 */

public class CommonWidgetActivity extends AppCompatActivity {

    private static final String TAG = "CommonWidgetActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_widget);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_layout_gravity)
    public void onLayoutGravityClick(View view){
        Intent intent = new Intent(this, LayoutGravityActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_flowlayout)
    public void onFLowLabelLayoutClick(){
        Intent intent = new Intent(this, FlowLayoutActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_toast)
    public void onShowToastClick(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CommonWidgetActivity.this, "哈哈", Toast.LENGTH_SHORT).show();
            }
        }, 5000);
    }

    @OnClick(R.id.btn_close_activity)
    public void closeActivity(View view){
        finish();
    }


    @OnClick(R.id.btn_marqueeview)
    public void onMarqueeViewClick(View view) {
        Intent intent = new Intent(this, MarqueeViewActivity.class);
        startActivity(intent);
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
    public void onConstraintLayoutClick(View view) {
        startActivity(new Intent(this, ConstraintLayoutActivity.class));
    }

    @OnClick(R.id.btn_bottom_layout)
    public void onBottomTabLayoutClick(View view) {
        startActivity(new Intent(this, BottomLayoutActivity.class));
    }

    @OnClick(R.id.btn_flex_layout)
    public void onFlexLayoutClick(View view){
        startActivity(new Intent(this, FlexLayoutActivity.class));
    }

    // btn_yoga_layout
    @OnClick(R.id.btn_yoga_layout)
    public void onYogaLayoutClick(View view){
        startActivity(new Intent(this, YogaLayoutActivity.class));
    }

    @OnClick(R.id.btn_scroll_conflict)
    public void onScrollConflictClick(View view){
        startActivity(new Intent(this, ScrollConflictActivity.class));
    }


    @OnClick(R.id.btn_scroll_conflict_demo1)
    public void onScrollConflictDemo1Click(View view){
        startActivity(new Intent(this, ScrollConflictDemo1Activity.class));
    }


    @OnClick(R.id.btn_scroll_conflict_demo2)
    public void onScrollConflictDemo2Click(View view){
        startActivity(new Intent(this, ScrollConflictDemo2Activity.class));
    }

    @OnClick(R.id.btn_water_fall)
    public void onWaterFallClick(View view){
        startActivity(new Intent(this, WaterFallActivity.class));
    }

    // btn_viewpager
    @OnClick(R.id.btn_viewpager)
    public void onViewPagerClick(View view){
        startActivity(new Intent(this, ViewPagerActivity.class));
    }

    //smart_refresh
    @OnClick(R.id.btn_smart_refresh)
    public void onSmartRefreshClick(View view){
        startActivity(new Intent(this, SmartRefreshActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
