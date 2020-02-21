package com.lucky.androidlearn.hotfix;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lucky.androidlearn.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.OnClick;

// 热修复示例
public class HotFixActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotfix);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_make_bug)
    public void onMakeCrashClick() {
        DivideZeroCrash crash = new DivideZeroCrash();
        crash.createCrash(this);
    }

    // TODO: 2020-02-20 经过测试 发现在7.0模拟器上是可以的 但是9.0就不行了 原因暂时不清楚
    @OnClick(R.id.btn_fix_bug)
    public void onFixCrashBugClick() {
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            // 修复bug
            // 首先我们需要将文件拷贝到 一个指定的目录下 然后让
            File dir = getDir("odex", Context.MODE_PRIVATE);
            File bugFixDexClassesFile = new File(dir, "classes2.dex");
            if (bugFixDexClassesFile.exists()) {
                bugFixDexClassesFile.delete();
            }
            inputStream = getAssets().open("classes.dex");
            // 拷贝到 // data/data/packagename/odex目录下
            fos = new FileOutputStream(bugFixDexClassesFile);
            // 文件读取流
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // 拷贝文件到指定目录后 开始篡改原始加载的类
        FixDexUtil.loadFixedDex(this);
        Toast.makeText(this, "修复好了", Toast.LENGTH_SHORT).show();
    }


}
