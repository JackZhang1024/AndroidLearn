package com.lucky.androidlearn.rxjava2.rximageloader;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class RxImageLoder {
    private static final String TAG = "RxImageLoder";
    private static volatile RxImageLoder mRxImageLoder;
    private String mUrl;
    private RequestCreator mRequestCreator;

    public RxImageLoder(Context context) {
        this.mRequestCreator = new RequestCreator(context);
    }


    public static RxImageLoder with(Context context) {
        if (mRxImageLoder == null) {
            synchronized (RxImageLoder.class) {
                if (mRxImageLoder == null) {
                    mRxImageLoder = new RxImageLoder.Builder(context).build();
                }
            }
        }
        return mRxImageLoder;
    }

    static class Builder {
        private Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }


        public RxImageLoder build() {
            return new RxImageLoder(mContext);
        }
    }

    public RxImageLoder load(String url) {
        this.mUrl = url;
        return this;
    }

    public void into(ImageView imageView) {
        Image defaultImage = new Image(mUrl, null);
        Observable.concat(mRequestCreator.getMemoryCacheImage(mUrl), mRequestCreator.getDishCacheImage(mUrl), mRequestCreator.getNetWorkCacheImage(mUrl))
                .filter(new Predicate<Image>() {
                    @Override
                    public boolean test(Image image) throws Exception {
                        return image != null && image.getBitmap() != null;
                    }
                })
                .first(defaultImage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Image>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Image image) {
                        Log.e(TAG, "onSuccess: "+image.getUrl());
                        imageView.setImageBitmap(image.getBitmap());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

}
