package com.lucky.androidlearn.rxjava2.rximageloader;

import android.graphics.Bitmap;


public class Image {
    private String url;
    private Bitmap bitmap;

    public Image(){
    }

    public Image(String url){
        this.url = url;
    }

    public Image(String url, Bitmap bitmap) {
        this.url = url;
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
