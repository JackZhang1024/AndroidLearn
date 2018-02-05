package com.lucky.androidlearn.launchmode;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.jingewenku.abrahamcaijin.commonutil.AppApplicationMgr;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.lucky.androidlearn.BuildConfig;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.core.util.MIMEUtil;
import com.lucky.androidlearn.dagger2learn.lesson04.ToastManager;

import java.io.File;
import java.util.List;

import javax.activation.MimeType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * task的概念，task是一个具有栈结构的对象，一个task可以管理多个Activity，
 * 启动一个应用，也就创建一个与之对应的task。
 * Created by zfz on 2017/11/25.
 */

public class LaunchModeActivity extends AppCompatActivity {

    private static final String TAG = "LaunchModeActivity";
    @BindView(R.id.tv_instance_name)
    TextView tvInstanceName;
    @BindView(R.id.tv_task_id)
    TextView tvTaskID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchmode);
        ButterKnife.bind(this);
        tvInstanceName.setText(this.toString());
        tvTaskID.setText(String.format("TaskID %s", this.getTaskId()));
    }

    // 每启动一次标准的Activity 就会生成一个新的Activity实例
    @OnClick(R.id.btn_standard)
    public void onStandardClick(View view) {
        Intent intent = new Intent(this, StandardActivity.class);
        startActivity(intent);
    }

    // 每启动一次LaunchMode为SingleTop的Activity
    // 就会在当前的Task中查看栈顶是否是对应Activity 如果有则重用 不用重新创建
    // 如果栈顶不是对应的(将要启动的)Activity 则会创建一个新的Activity实例
    @OnClick(R.id.btn_singletop)
    public void onSingleTopClick(View view) {
        Intent intent = new Intent(this, SingleTopActivity.class);
        startActivity(intent);
    }

    // 每次启动一个LaunchMode为SingleTask的Activity
    // 会查看当前栈中否是存在将要启动的Activity, 如果不存在 则在栈顶创建
    // 如果存在 则会将将栈中位于此Activity之上的Activity全部弹出栈，然后该Activity位于栈顶
    @OnClick(R.id.btn_singletask)
    public void onSingleTaskClick(View view) {
        Intent intent = new Intent(this, SingleTaskActivity.class);
        startActivity(intent);
    }

    // 这种启动模式比较特殊，因为它会启用一个新的栈结构，将Activity放置于这个新的栈结构中，
    // 并保证不再有其他Activity实例进入。
    @OnClick(R.id.btn_singleinstance)
    public void onSingleInstanceClick(View view) {
        Intent intent = new Intent(this, SingleInstanceActivity.class);
        startActivity(intent);
    }

    // 启动分享页 单独存在于一个Task中
    @OnClick(R.id.btn_launch_share)
    public void onLaunchShareClick(View view) {
        Intent intent = new Intent("android.intent.action.SINGLE_INSTANCE_SHARE");
        startActivity(intent);
    }

    @OnClick(R.id.btn_launch_exclude_from_recents)
    public void onLaunchExcluedFromRecentsClick(View view) {
        Intent intent = new Intent(this, ExcludeFromRecentsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

    // 启动分享页 单独存在于一个Task中
    @OnClick(R.id.btn_launch_another_app)
    public void onLaunchAnotherAppClick(View view) {
        launchAnotherApp();
    }

    // Scheme跳转协议
    private void launchAnotherApp() {
        if (AppApplicationMgr.isInstalled(this, "com.lucky.customviewlearn")) {
            String aciton = "com.aragoncs.launch";
            String uri = "aragoncs://scan?clientId=1&userId=2";
            Intent intent = new Intent(aciton);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }
    }

    @OnClick(R.id.btn_launch_browser_pdf)
    public void onOpenPDFClick() {
        String filePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "hello.pdf";
        Log.e(TAG, "onOpenPDFClick: " + filePath);
        openPDF(filePath);
    }

    private void openPDF(String filePath) {
        try {
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", new File(filePath));
            } else {
                uri = Uri.fromFile(new File(filePath));
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            //intent.setType("application/pdf");
            String mimeType = MIMEUtil.getMIMEType(new File(filePath));
            intent.setType(mimeType);
            List<ResolveInfo> activityInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (activityInfoList != null && activityInfoList.size() > 0) {
                Intent realIntent = new Intent();
                realIntent.setAction(Intent.ACTION_VIEW);
                realIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                realIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                realIntent.setDataAndType(uri, "application/pdf");
                startActivity(realIntent);
            } else {
                AppToastMgr.show(this, "没有找到对应的程序");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAnotherApp() {
        //直接从入口的 天猫进到支付宝
        //从入口跳转到支付宝个人信息页面 一闪而过
        //需要Tunity那块的配合处理
        String packageName = "com.aragoncs.applaunch";
        String activityName = "com.aragoncs.applaunch.Main2Activity";
        String action = "com.aragon.test";
        ComponentName name = new ComponentName(packageName, activityName);
        Intent intent = new Intent(action);
        // intent.setAction(action);
        intent.setComponent(name);
        intent.putExtra("EXTRA_PARAM0", "HELLO");
        intent.putExtra("EXTRA_PARAM1", "WORLD");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void startTunity() {
        try {
            String client_id = "18210575531";
            String user_id = "1234567";
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.tunityapp.tunityapp", PackageManager.GET_ACTIVITIES);
            StringBuffer sbf = new StringBuffer();
            sbf.append("tunity://scan?");
            sbf.append("client_id=");
            sbf.append(client_id);
            sbf.append("&");
            sbf.append("user_id=");
            sbf.append(user_id);
            String uri = sbf.toString();
            Intent intent = new Intent("com.google.android.gms.appinvite.ACTION_PREVIEW");
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // No Tunity app - Download
            String url = "market://details?id=com.tunityapp.tunityapp";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    private void startGoogleService() {
        //https://play.google.com/store/apps/details?id="
        try {
            String url = "market://details?id=com.tunityapp.tunityapp";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception e) {
            String url = "https://play.google.com/store/apps/details?id=com.tunityapp.tunityapp";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }


}
