package com.lucky.androidlearn.rxjava2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.rxjava2.task.LoadBookListTask;
import com.lucky.androidlearn.rxjava2.task.TaskLauncher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zfz
 * Created by zfz on 2018/3/18.
 *
 * https://www.cnblogs.com/ganchuanpu/p/7136150.html
 */

public class RxJavaMergeActivity extends AppCompatActivity {
    private static final String TAG = "RxJavaMergeActivity";
    @BindView(R.id.tv_merge_result)
    TextView tvMergeResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_merge);
        ButterKnife.bind(this);
    }

    /**
     * merge操作符可能将两个Observable的数据进行交错组合
     * concat操作符是按照Observable加入的顺序来组合
     */
    @OnClick(R.id.btn_rxjava_merge)
    public void mergeData() {
        Observable.merge(getDataFromLocal(), getDataFromNet())
        //Observable.concat(getDataFromLocal(), getDataFromNet())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {

                    StringBuffer stringBuffer = new StringBuffer();

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        for (String book : strings) {
                            Log.e(TAG, "onNext: "+book);
                            stringBuffer.append(book);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: ");
                        tvMergeResult.setText(stringBuffer.toString());
                    }
                });
    }

    /**
     * 加载本地数据
     */
    private Observable<List<String>> getDataFromLocal() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Android开发艺术探索");
        stringList.add("Android群英传");
        stringList.add("Android开发权威指南");
        stringList.add("Android设计模式");
        return Observable.just(stringList);
    }


    /**
     * 模拟从网络加载数据
     */
    private Observable<List<String>> getDataFromNet() {
        List<String> bookList = new ArrayList<>();
        try {
            bookList = new TaskLauncher<List<String>>().execute(new LoadBookListTask());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Observable.just(bookList).subscribeOn(Schedulers.io());
    }


}
