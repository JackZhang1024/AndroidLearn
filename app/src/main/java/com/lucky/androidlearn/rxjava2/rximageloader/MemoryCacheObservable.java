package com.lucky.androidlearn.rxjava2.rximageloader;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

public class MemoryCacheObservable extends CacheObservable {
    private static final String TAG = "MemoryCacheObservable";
    private int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
    private int maxSize = maxMemory / 8;

    private LruCache<String, Bitmap> bitmapLruCache = new LruCache<String, Bitmap>(maxSize) {

        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
        }
    };

    @Override
    public Image getDataFromCache(String url) {
        Image image = new Image(url);
        Bitmap bitmap = bitmapLruCache.get(url);
        if (bitmap != null) {
            Log.e(TAG, "getDataFromCache: ");
            image.setBitmap(bitmap);
            return image;
        }
        return image;
    }

    @Override
    public void putImageToCache(Image image) {
        bitmapLruCache.put(image.getUrl(), image.getBitmap());
    }
}
