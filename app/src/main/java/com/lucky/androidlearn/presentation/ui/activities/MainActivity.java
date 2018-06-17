package com.lucky.androidlearn.presentation.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.animation.AnimationMainActivity;
import com.lucky.androidlearn.annotation.AnnotationActivity;
import com.lucky.androidlearn.annotation.SimpleButterKnifeActivity;
import com.lucky.androidlearn.dagger2learn.DaggerLearnMainActivity;
import com.lucky.androidlearn.domain.executor.impl.ThreadExecutor;
import com.lucky.androidlearn.handler.HandlerLearnActivity;
import com.lucky.androidlearn.handler.HandlerThreadActivity;
import com.lucky.androidlearn.ipc.IPCLearnActivity;
import com.lucky.androidlearn.math.MathActivity;
import com.lucky.androidlearn.media.MediaActivity;
import com.lucky.androidlearn.mvc.MVCMainActivity;
import com.lucky.androidlearn.mvp.MVPMainActivity;
import com.lucky.androidlearn.mvvm.MVVMMainActivity;
import com.lucky.androidlearn.permission.PermissionManageActivity;
import com.lucky.androidlearn.presentation.presenters.MainPresenter;
import com.lucky.androidlearn.presentation.presenters.impl.MainPresenterImpl;
import com.lucky.androidlearn.provider.MainProviderActivity;
import com.lucky.androidlearn.reference.ReferenceActivity;
import com.lucky.androidlearn.rxjava2.RxJavaActivity;
import com.lucky.androidlearn.webservice.demo.WebServiceActivity;
import com.lucky.androidlearn.widget.common.AppForegroundActivity;
import com.lucky.androidlearn.launchmode.LaunchModeActivity;
import com.lucky.androidlearn.storage.WelcomeMessageRepository;
import com.lucky.androidlearn.threading.MainThreadImpl;
import com.lucky.androidlearn.widget.common.CommonWidgetActivity;
import com.lucky.androidlearn.widget.screen.ScreenDensityActivity;
import com.lucky.androidlearn.service.ServiceActivity;
import com.lucky.androidlearn.xml.XmlActivity;
import com.lucky.androidlearn.kotlin.KotlinLearnMainActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * MVP设计模式:
 * 1.设计一个Presenter接口
 * 2.在Presenter接口中再写一个View接口
 * 3.实现Presenter接口 PresenterImpl 这个实现的构造方法中有View
 * 4.在Activity实现View接口
 * 5.在Activity中设定一个Presenter接口对象
 * 6.触发事件用Presenter来调用其中定义好的方法
 * 7.触发事件处理结果用View的接口回调来处理用户的交互结果
 * <p>
 * 总结：简洁、高效
 */
public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private static final String TAG = "MainActivity";

    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter = new MainPresenterImpl(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(),
                this, new WelcomeMessageRepository());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mMainPresenter.resume();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mMainPresenter.destroy();
    }


    @OnClick(R.id.main_view)
    public void onMainViewClick(View view) {
        Intent intent = new Intent(this, CommonWidgetActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_service)
    public void onServiceClick(View view) {
        Intent intent = new Intent(this, ServiceActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_launch_mode)
    public void onLaunchModeClick(View view) {
        Intent intent = new Intent(this, LaunchModeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_run_foreground)
    public void onCheckAppRunningForeground() {
        Intent intent = new Intent(this, AppForegroundActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_screen_density)
    public void onScreenAdaptViewClick(View view) {
        Intent intent = new Intent(this, ScreenDensityActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_media)
    public void onMultiMediaClick(View view) {
        Intent intent = new Intent(this, MediaActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_permission)
    public void onPermissionManageClick(View view) {
        Intent intent = new Intent(this, PermissionManageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_xml)
    public void onXMlLearnClick(View view) {
        Intent intent = new Intent(this, XmlActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_webservice)
    public void onWebServiceClick(View view) {
        startActivity(new Intent(this, WebServiceActivity.class));
    }

    @OnClick(R.id.btn_reference)
    public void onReferenceLearnClick(View view) {
        Intent intent = new Intent(this, ReferenceActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_animation)
    public void onAnimationLearnClick(View view) {
        Intent intent = new Intent(this, AnimationMainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_provider)
    public void onProviderLearnClick(View view) {
        Intent intent = new Intent(this, MainProviderActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_math_util)
    public void onMathClick(View view) {
        Intent intent = new Intent(this, MathActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_annotation)
    public void onAnnotationClick(View view) {
        Intent intent = new Intent(this, AnnotationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_annotation_processor)
    public void onAnnotationProcessorClick(View view) {
        Intent intent = new Intent(this, SimpleButterKnifeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_architecture_mvc)
    public void onMVCArchitectureClick(View view) {
        Intent intent = new Intent(this, MVCMainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_architecture_mvp)
    public void onMVPArchitectureClick(View view) {
        Intent intent = new Intent(this, MVPMainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_architecture_mvvm)
    public void onMVVMArchitectureClick(View view) {
        Intent intent = new Intent(this, MVVMMainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_rxjava)
    public void onRxJavaClick(View view) {
        Intent intent = new Intent(this, RxJavaActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_handler_learn)
    public void onHandlerClick() {
        Intent intent = new Intent(this, HandlerLearnActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_handler_thread_learn)
    public void onHandlerThreadLearn(){
        Intent intent = new Intent(this, HandlerThreadActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_dagger2_main)
    public void onDagger2MainLearn(){
        Intent intent = new Intent(this, DaggerLearnMainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_ipc_learn)
    public void onIPCLearn(){
        Intent intent = new Intent(this, IPCLearnActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_kotlin_learn)
    public void onKotlinLearn(){
        Intent intent = new Intent(this, KotlinLearnMainActivity.class);
        startActivity(intent);
    }

}
