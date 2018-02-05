package com.lucky.androidlearn.rxjava2.rximageloader;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class RequestCreator {

    private CacheObservable memoryCacheObservable;
    private CacheObservable diskCacheObservable;
    private CacheObservable netWorkCacheObservable;

    RequestCreator(Context context) {
        memoryCacheObservable = new MemoryCacheObservable();
        diskCacheObservable   = new DiskCacheObservable(context);
        netWorkCacheObservable= new NetWorkCacheObservable();
    }

    public Observable<Image> getMemoryCacheImage(String url) {
        return memoryCacheObservable.getImage(url).filter(new Predicate<Image>() {
            @Override
            public boolean test(Image image) throws Exception {
                return image != null && image.getBitmap()!=null;
            }
        });
    }


    public Observable<Image> getDishCacheImage(String url) {
        return diskCacheObservable.getImage(url).filter(new Predicate<Image>() {
            @Override
            public boolean test(Image image) throws Exception {
                return image != null && image.getBitmap()!=null;
            }
        }).doOnNext(new Consumer<Image>() {
            @Override
            public void accept(Image image) throws Exception {
                memoryCacheObservable.putImageToCache(image);
            }
        });
    }

    public Observable<Image> getNetWorkCacheImage(String url) {
        return netWorkCacheObservable.getImage(url)
                .filter(new Predicate<Image>() {
                    @Override
                    public boolean test(Image image) throws Exception {
                        return image != null && image.getBitmap()!=null;
                    }
                })
                .doOnNext(new Consumer<Image>() {
                    @Override
                    public void accept(Image image) throws Exception {
                        // 将网络下载的图片缓存到本地和内存缓存中
                        diskCacheObservable.putImageToCache(image);
                        memoryCacheObservable.putImageToCache(image);
                    }
                });
    }


}
