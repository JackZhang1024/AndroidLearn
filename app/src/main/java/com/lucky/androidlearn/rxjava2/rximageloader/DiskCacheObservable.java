package com.lucky.androidlearn.rxjava2.rximageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.lucky.androidlearn.core.util.BitmapUtils;
import com.lucky.androidlearn.core.util.DiskLruCacheManager;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;

public class DiskCacheObservable extends CacheObservable {
    private static final String TAG = "DiskCacheObservable";
    private Context mContext;

    public DiskCacheObservable(Context context) {
        this.mContext = context;
    }

    @Override
    public Image getDataFromCache(String url) {
        Image image = new Image(url);
        Bitmap bitmap = loadDataFromDisk(url);
        if (bitmap != null) {
            image.setBitmap(bitmap);
            Log.e(TAG, "getDataFromCache: ");
            return image;
        }
        return image;
    }

    @Override
    public void putImageToCache(Image image) {
        try {
            InputStream inputStream = getBitmapInputStream(image);
            if (inputStream != null) {
                DiskLruCacheManager.getInstance().saveStream(image.getUrl(), inputStream);
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadDataFromDisk(String url) {
        Bitmap bitmap = null;
        try {
            DiskLruCacheManager diskLruCacheManager = DiskLruCacheManager.getInstance();
            diskLruCacheManager.open(mContext, "android_learn");
            InputStream inputStream = diskLruCacheManager.getInputStream(url);
            FileInputStream fileInputStream = (FileInputStream) inputStream;
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            if (fileDescriptor != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private InputStream getBitmapInputStream(Image image) {
        try {
            File dir = mContext.getExternalCacheDir();
            File tempFile = new File(dir, "temp_pic.jpg");
            BitmapUtils.saveBitMapToFile(image.getBitmap(), tempFile);
            FileInputStream fileInputStream = new FileInputStream(tempFile);
            return fileInputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
