package com.lucky.androidlearn.rxjava2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.rxjava2.rximageloader.RxImageLoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;


/**
 * 仿Picasso图片加载
 *
 * @author zfz
 */
public class RxJavaImageDownloadActivity extends AppCompatActivity {
    private static final String TAG = "RxJavaImageDownload";
    @BindView(R.id.iv_show)
    ImageView imageViewShow;
    @BindView(R.id.btn_download_image)
    Button btnLoadImage;

    Observable<String> memoryCache;
    Observable<String> diskCache;
    Observable<String> netCache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_image_download);
        ButterKnife.bind(this);
        memoryCache = loadMemoryCache();
        diskCache = loadDiskCache();
        netCache = loadNetCache();
        loadImageView();
    }


    private Observable<String> loadMemoryCache() {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) {
                e.onNext("memoryCache");
                //e.onNext("");
                e.onComplete();
            }
        });
    }


    private Observable<String> loadDiskCache() {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) {
                e.onNext("diskCache");
                e.onComplete();
            }
        });

    }


    private Observable<String> loadNetCache() {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) {
                e.onNext("netCache");
                e.onComplete();
            }
        });
    }


    private void loadImageView() {
        RxView.clicks(btnLoadImage).subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object o) {
//                Observable.concat(memoryCache, diskCache, netCache)
//                        .filter(s -> !TextUtils.isEmpty(s))
//                        .first("memoryCache")
//                        .subscribe(s-> Log.e(TAG, "accept: "+s));

                String url = "http://p2.so.qhimgs1.com/t01fb6de990d05c7752.jpg";
                RxImageLoder.with(RxJavaImageDownloadActivity.this).load(url).into(imageViewShow);
            }
        });


    }

}
