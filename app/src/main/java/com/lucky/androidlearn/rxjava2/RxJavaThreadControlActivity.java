package com.lucky.androidlearn.rxjava2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.rxjava2.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zfz
 *         Created by zfz on 2018/3/17.
 */

public class RxJavaThreadControlActivity extends AppCompatActivity {

    private static final String TAG = "RxJavaThreadControl";

    @BindView(R.id.tv_user_profile)
    TextView tvUserProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_thread_control);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_load_profile)
    public void loadProfile() {
        Observable<User> observable = getObservable();
        Observer<User> observer = getObserver();
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    private Observer<User> getObserver() {
        return new Observer<User>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe: ");
                tvUserProfile.setText("");
            }

            @Override
            public void onNext(User user) {
                Log.e(TAG, "onNext: " + user);
                tvUserProfile.setText(user.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        };
    }


    private Observable<User> getObservable() {
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> e) throws Exception {
                new Thread(new LoadProfileTask(e)).start();
            }
        });
    }


    /**
     * 模拟网络加载数据
     */
    class LoadProfileTask implements Runnable {
        ObservableEmitter<User> observableEmitter;

        public LoadProfileTask(ObservableEmitter<User> emitter) {
            observableEmitter = emitter;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                User user = new User("Jack", 20, "http://www.baidu.com");
                observableEmitter.onNext(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
