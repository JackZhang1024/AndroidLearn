package com.lucky.androidlearn.rxjava2;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.lucky.androidlearn.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 关键字搜索
 *
 * 每隔200毫秒采取执行一次搜索操作
 * flapMap
 * switchMap 指进行最近的一次操作 之前的操作都被舍弃
 *
 * @author zfz
 * Created by zfz on 2018/3/17.
 */

public class RxJavaKeyWordsSearchActivity extends AppCompatActivity {
    private static final String TAG = "RxJavaKeyWordsSearch";
    @BindView(R.id.et_input_search)
    EditText mEtSearchKeyWords;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_keywords_search);
        ButterKnife.bind(this);
        doSearch();
    }

    public void doSearch(){
        RxTextView.textChanges(mEtSearchKeyWords)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence charSequence) throws Exception {
                        // 过滤数据 满足条件才能继续进行
                        Log.e(TAG, "test: "+charSequence);
                        return charSequence.length()> 0;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .switchMap(new Function<CharSequence, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(CharSequence charSequence) throws Exception {
                        List<String> stringList = new ArrayList<>();
                        stringList.add("Ada0");
                        stringList.add("Adaa");
                        stringList.add("Adab");
                        stringList.add("Adac");
                        stringList.add("Adad");
                        return Observable.just(stringList);
                    }
                })
//                .flatMap(new Function<CharSequence, ObservableSource<List<String>>>() {
//                    @Override
//                    public ObservableSource<List<String>> apply(CharSequence charSequence) throws Exception {
//                        List<String> stringList = new ArrayList<>();
//                        stringList.add("Ada0");
//                        stringList.add("Adaa");
//                        stringList.add("Adab");
//                        stringList.add("Adac");
//                        stringList.add("Adad");
//                        return Observable.just(stringList);
//                    }
//                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        Log.e(TAG, "accept: "+strings);
                    }
                });

    }



}
