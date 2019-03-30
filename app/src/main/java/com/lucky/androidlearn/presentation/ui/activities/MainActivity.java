package com.lucky.androidlearn.presentation.ui.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.jingewenku.abrahamcaijin.commonutil.AppScreenMgr;
import com.jingewenku.abrahamcaijin.commonutil.klog.KLog;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.animation.AnimationMainActivity;
import com.lucky.androidlearn.annotation.AnnotationActivity;
import com.lucky.androidlearn.annotation.SimpleButterKnifeActivity;
import com.lucky.androidlearn.crosswalk.CrossWalkActivity;
import com.lucky.androidlearn.dagger2learn.DaggerLearnMainActivity;
import com.lucky.androidlearn.domain.executor.impl.ThreadExecutor;
import com.lucky.androidlearn.encrypt.EncryptDecryptActivity;
import com.lucky.androidlearn.eventchange.AttributeChangeListenActivity;
import com.lucky.androidlearn.exception.ExceptionSummaryActivity;
import com.lucky.androidlearn.filelearn.FileLearnActivity;
import com.lucky.androidlearn.handler.HandlerLearnActivity;
import com.lucky.androidlearn.handler.HandlerThreadActivity;
import com.lucky.androidlearn.hybrid.WebViewActivity;
import com.lucky.androidlearn.ipc.IPCLearnActivity;
import com.lucky.androidlearn.j2v8.J2V8LearnActivity;
import com.lucky.androidlearn.json.JsonLearnActivity;
import com.lucky.androidlearn.lru.LRUActivity;
import com.lucky.androidlearn.math.MathActivity;
import com.lucky.androidlearn.media.MediaActivity;
import com.lucky.androidlearn.multithread.MultiThreadActivity;
import com.lucky.androidlearn.mvc.MVCMainActivity;
import com.lucky.androidlearn.mvp.MVPMainActivity;
import com.lucky.androidlearn.mvvm.MVVMMainActivity;
import com.lucky.androidlearn.network.NetWorkTrafficActivity;
import com.lucky.androidlearn.network.traffic.TrafficMonitorService;
import com.lucky.androidlearn.okhttplearn.OkHttpLearnActivity;
import com.lucky.androidlearn.performance.PerformanceOptimizationActivity;
import com.lucky.androidlearn.permission.PermissionManageActivity;
import com.lucky.androidlearn.presentation.presenters.MainPresenter;
import com.lucky.androidlearn.presentation.presenters.impl.MainPresenterImpl;
import com.lucky.androidlearn.provider.MainProviderActivity;
import com.lucky.androidlearn.reference.ReferenceActivity;
import com.lucky.androidlearn.rxjava2.RxJavaActivity;
import com.lucky.androidlearn.security.SecurityCheckActivity;
import com.lucky.androidlearn.umeng.UMengActivity;
import com.lucky.androidlearn.webservice.demo.WebServiceActivity;
import com.lucky.androidlearn.widget.common.AppForegroundActivity;
import com.lucky.androidlearn.launchmode.LaunchModeActivity;
import com.lucky.androidlearn.storage.WelcomeMessageRepository;
import com.lucky.androidlearn.threading.MainThreadImpl;
import com.lucky.androidlearn.widget.common.CommonWidgetActivity;
import com.lucky.androidlearn.widget.material.MaterialWidgetActivity;
import com.lucky.androidlearn.widget.screen.ScreenDensityActivity;
import com.lucky.androidlearn.service.ServiceActivity;
import com.lucky.androidlearn.xml.XmlActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.nio.charset.Charset;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

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
        getContentViewHeight();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter = new MainPresenterImpl(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(),
                this, new WelcomeMessageRepository());
        //startTrafficMonitor();
        //UMConfigure.init(this, "5bdbef32b465f5b32400001d", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //MobclickAgent.setDebugMode(true);
        //MobclickAgent.openActivityDurationTrack(false);
        //MobclickAgent.setSessionContinueMillis(10000);
        //MobclickAgent.setCatchUncaughtExceptions(true);
        String abc = "hello,123";
//        byte[] bytes = abc.getBytes(Charset.defaultCharset());
//        for (byte i:bytes){
//            System.out.println(i);
//        }

    }

    private void startTrafficMonitor(){
        startService(new Intent(this, TrafficMonitorService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mMainPresenter.resume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int height = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
                int titleBarHeight = AppScreenMgr.getTitleBarHeight(MainActivity.this);
                System.out.println("contentHeight "+height+"  titleBarHeight "+titleBarHeight);
            }
        }, 1000);
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
        stopService(new Intent(this, TrafficMonitorService.class));
    }


    @OnClick(R.id.btn_multi_thread)
    public void onMultiThreadClick(View view){
        Intent intent = new Intent(this, MultiThreadActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.main_view)
    public void onMainViewClick(View view) {
        Intent intent = new Intent(this, CommonWidgetActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.material_view)
    public void onMaterialViewClick(View view) {
        Intent intent = new Intent(this, MaterialWidgetActivity.class);
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


    @OnClick(R.id.btn_crosswalk)
    public void onCrossWalkLearn(){
        Intent intent = new Intent(this, CrossWalkActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_j2v8_learn)
    public void onJ2V8Learn(){
        Intent intent = new Intent(this, J2V8LearnActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_security_check)
    public void onSecurityCheck(){
        Intent intent = new Intent(this, SecurityCheckActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_json_parse)
    public void onJsonParse(){
        Intent intent = new Intent(this, JsonLearnActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_attribute_change)
    public void onAttributeChange(){
        Intent intent = new Intent(this, AttributeChangeListenActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_network_traffic)
    public void onNetWorkTrafficListen(){
        Intent intent = new Intent(this, NetWorkTrafficActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_exception_summary)
    public void onExceptionSummaryClick(View view){
        Intent intent = new Intent(this, ExceptionSummaryActivity.class);
        startActivity(intent);
    }

    //btn_umeng
    @OnClick(R.id.btn_umeng)
    public void onUmengClick(View view){
        Intent intent = new Intent(this, UMengActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_webview)
    public void onWebViewClick(View view){
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_file_learn)
    public void onFileLearnClick(View view){
        Intent intent = new Intent(this, FileLearnActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_performance_optimization)
    public void onPerformanceClick(View view){
        Intent intent = new Intent(this, PerformanceOptimizationActivity.class);
        startActivity(intent);
    }

    // btn_encrypt
    @OnClick(R.id.btn_encrypt)
    public void onEncryptDecryptClick(View view){
        Intent intent = new Intent(this, EncryptDecryptActivity.class);
        startActivity(intent);
    }

    // btn_lru_algorithm
    @OnClick(R.id.btn_lru_algorithm)
    public void onLRUAlgorithmClick(View view){
        Intent intent = new Intent(this, LRUActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_okhttp)
    public void onOKHttpLearnClick(View view){
        Intent intent = new Intent(this, OkHttpLearnActivity.class);
        startActivity(intent);
    }


    private void getContentViewHeight(){
        int screenHeight = AppScreenMgr.getScreenHeight(this);
        int statusHeight = AppScreenMgr.getStatusHeight(this);
        int statusBarHeight = AppScreenMgr.getStatusBarHeight(this);
        System.out.println("screenHeight "+screenHeight+" statusHeight "+statusHeight+" statusBarHeight "+statusBarHeight);
    }


}
