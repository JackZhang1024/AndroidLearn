package com.lucky.androidlearn.plugin.hookplugin;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

// Hook的方式插件化
public class HookPluginMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hookplugin_main);
        Button btnHookPlugin = findViewById(R.id.btn_hook_plugin);
        btnHookPlugin.setOnClickListener(v -> {
            // android.content.ActivityNotFoundException:
            // Unable to find explicit activity class {com.lucky.androidlearn/com.lucky.androidlearn.plugin.hookplugin.TestPluginActivity};
            // have you declared this activity in your AndroidManifest.xml?

            startActivity(new Intent(this, TestPluginActivity.class));
            // TestPluginActivity  hook startActivity ProxyActivity AMS (通过检测)

            // public static IActivityManager getService()
            // IActivityManager start
            // public interface IActivityManager extends IInterface {
            //    public int startActivity() throws RemoteException;
            //}

            // 我们使用自己的IActivityManager 代替系统的IActivityManager对象
            // 在系统的IActivityManager执行startActivity的时候启动ProxyActivity（我们提前注册好的Activity）
            // 这样就可以用真的注册过的Activity来绕过AMS检查 这样就不会报错了
            // 同时我们可以将组建信息存在这个新的启动的代理Intent中，方便后面还原


        });
        Button btnStartActivityInPlugin = findViewById(R.id.btn_hook_plugin_in_apk);
        btnStartActivityInPlugin.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.luckyboy.plugin_package", "com.luckyboy.plugin_package.MainActivity"));
            startActivity(intent);
        });
    }
    
}
