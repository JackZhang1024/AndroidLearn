package com.lucky.androidlearn.filelearn;

import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


//https://www.cnblogs.com/lilinjie/p/7065386.html
public class FileProcessLearnActivity extends AppCompatActivity {

    private static final String TAG = "FileProcessLearnActivit";
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

    public void makeFile(View view) throws IOException {
        String filePath = loadFilePath();
        Log.e(TAG, "makeFile: loadFilePath "+filePath);
        File fileCreated = new File(filePath);
        if (fileCreated.exists()) {
            fileCreated.delete();
        }
        fileCreated.createNewFile();
        Log.e(TAG, "makeFile: path " + fileCreated.getAbsolutePath());
        FileWriter fileWriter = new FileWriter(fileCreated);
        fileWriter.write("阿拉啦啦啦啦");
        fileWriter.flush();
        FileReader fileReader = new FileReader(fileCreated);
        fileWriter.close();
        char[] chars = new char[1024];
        int length = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while ((length = fileReader.read(chars)) != -1) {
            stringBuilder.append(new String(chars, 0, length));
        }
        Log.e(TAG, "读取文件result " + stringBuilder.toString());
        fileReader.close();
    }

    private String loadFilePath() {
        String fileName = "HelloWorld.txt";
        //return Environment.getDownloadCacheDirectory().getAbsolutePath()+File.separator+fileName; ///data/cache/HelloWorld.txt
        //return Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+fileName;
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+File.separator+fileName;
    }


}
