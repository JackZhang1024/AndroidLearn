package com.lucky.androidlearn.filelearn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.lucky.androidlearn.R;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


//https://www.cnblogs.com/lilinjie/p/7065386.html
public class FileProcessLearnActivity extends AppCompatActivity {

    @BindView(R.id.btn_package_files)
    Button mTvCacheDir;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filelearn);
        ButterKnife.bind(this);
        showDirs();
    }

    public void showDirs() {
        File cacheFileDir = getCacheDir();
        File fileDir = getFilesDir();
    }


    @OnClick(R.id.btn_package_files)
    public void createPackageFile() {
        File packageFile = getFilesDir();
        File targetFile = new File(packageFile, "Hello.txt");
        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            System.out.println("path " + targetFile.getAbsolutePath());
            FileWriter fileWriter = new FileWriter(targetFile);
            fileWriter.write("hahhahahhd");
            fileWriter.flush();
            FileReader fileReader = new FileReader(targetFile);
            fileWriter.close();
            char[] chars = new char[1024];
            int length = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while ((length = fileReader.read(chars)) != -1) {
                stringBuilder.append(new String(chars, 0, length));
            }
            System.out.println("result " + stringBuilder.toString());
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
