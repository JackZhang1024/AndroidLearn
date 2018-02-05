package com.lucky.androidlearn.dagger2learn.learn00;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lucky.androidlearn.dagger2learn.learn00.model.Pot;
import com.lucky.androidlearn.dagger2learn.learn00.model.Rose;

import javax.inject.Inject;


/**
 * @author zfz
 */
public class Dagger2Main0Activity extends AppCompatActivity {
    private static final String TAG = "Dagger2Main0";

    //第一种写法
    //Pot pot;

    //Inject 方法作用有三个
    //一: 构造器注入
    //   1. 标记构造方法 告诉Dagger可以利用该构造方法构建对象
    //   2. 注入构造器所需的参数依赖, 如在将Rose注入到Pot构造器中, 但是Inject只能标记一个构造器 不能标注多个构造器
    //二：属性注入
    //   1. 在Dagger2Main0Activity的Pot属性上进行注入，但是该属性不能为private, 否则不能注入 属性注入是最常见的注入方式
    //三：方法注入

    //第二种写法
    //@Inject
    //Pot pot;

    private Pot pot;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //lesson01();
        //lesson02();
        lesson03();
    }


    private void lesson01() {
        // 第一种写法： 不使用Dagger依赖注入
//        Rose rose = new Rose();
//        pot = new Pot(rose);
//        String whisper = pot.show();
//        Log.e(TAG, "onCreate: "+whisper);
    }

    private void lesson02() {
        // 第二种写法: 使用Dagger2进行依赖注入
        DaggerDagger2Main0Component.create().inject(this);
        String whisper = pot.show();
        Log.e(TAG, "onCreate: " + whisper);
    }


    private void lesson03(){
        // 第三种利用setPot()方法来进行注入
        DaggerDagger2Main0Component.create().inject(this);
        String whisper = pot.show();
        Log.e(TAG, "onCreate: " + whisper);
    }

    @Inject
    public void setPot(Pot pot){
        this.pot = pot;
    }

    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     * For more information, see Java Concurrency in Practice.
     *
     * @Inject
     * void setupListeners() {
     *    mTasksView.setPresenter(this);
     * }
     *
     */


}
