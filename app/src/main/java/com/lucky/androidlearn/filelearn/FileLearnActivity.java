package com.lucky.androidlearn.filelearn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import butterknife.ButterKnife;
import butterknife.OnClick;


//https://www.cnblogs.com/lilinjie/p/7065386.html
public class FileLearnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filelearn);
        ButterKnife.bind(this);
    }

    private void showDirs(){
        File cacheFileDir = getCacheDir();
        System.out.println("cacheFileDir "+cacheFileDir.getAbsolutePath());
        File fileDir = getFilesDir();
        System.out.println("fileDir "+fileDir.getAbsolutePath());
        File dataFileDir = getDataDir();
    }


    @OnClick(R.id.btn_package_files)
    public void createPackageFile() {
        File packageFile = getFilesDir();
        File targetFile = new File(packageFile, "Hello.txt");
        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            System.out.println("path "+targetFile.getAbsolutePath());
            FileWriter fileWriter = new FileWriter(targetFile);
            fileWriter.write("hahhahahhd");
            fileWriter.flush();
            FileReader fileReader = new FileReader(targetFile);
            fileWriter.close();
            char[] chars= new char[1024];
            int length=0;
            StringBuilder stringBuilder = new StringBuilder();
            while ((length=fileReader.read(chars))!=-1){
                stringBuilder.append(new String(chars, 0, length));
            }
            System.out.println("result "+stringBuilder.toString());
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
