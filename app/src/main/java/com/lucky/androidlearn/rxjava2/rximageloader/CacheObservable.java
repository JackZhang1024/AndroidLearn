package com.lucky.androidlearn.rxjava2.rximageloader;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public abstract class CacheObservable {

    public Observable<Image> getImage(String url) {
        return Observable.create(e -> {
            Image image = getDataFromCache(url);
            e.onNext(image);
            e.onComplete();
        });
    }

    public abstract Image getDataFromCache(String url);

    public abstract void putImageToCache(Image image);

}
