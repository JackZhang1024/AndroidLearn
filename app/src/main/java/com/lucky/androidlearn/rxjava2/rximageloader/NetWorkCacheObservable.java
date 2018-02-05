package com.lucky.androidlearn.rxjava2.rximageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.lucky.androidlearn.core.util.BitmapUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class NetWorkCacheObservable extends CacheObservable {

    private static final String TAG = "NetWorkCacheObservable";

    @Override
    public Image getDataFromCache(String url) {
        Image image = new Image(url);
        Bitmap bitmap = downLoadBitmap(url);
        if (bitmap != null) {
            Log.e(TAG, "getDataFromCache: ");
            image.setBitmap(bitmap);
            return image;
        }
        return image;
    }

    @Override
    public void putImageToCache(Image image) {
        Log.e(TAG, "putImageToCache: "+image.getUrl());
    }

    private Bitmap downLoadBitmap(String url) {
        Bitmap bitmap = null;
        try {
            URL requestUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
