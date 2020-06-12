package com.lucky.androidlearn.window;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.dagger2learn.learn04.ToastManager;


// 浮动窗口
public class WindowManagerActivity extends AppCompatActivity {

    private static final String TAG = "WindowManagerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windowmanager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: " + hasPermission());
        if (!hasPermission()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 100);
        } else {
            FloatView floatView = new FloatView(this);
            floatView.setImageResource(R.drawable.ic_circle);
            floatView.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT > 24) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (hasPermission()) {
            FloatView floatView = new FloatView(this);
            floatView.setImageResource(R.drawable.ic_circle);
            floatView.show();
        } else {
            Toast.makeText(this, "权限拒绝了", Toast.LENGTH_SHORT).show();
        }
    }


}
