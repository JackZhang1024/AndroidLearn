package com.lucky.androidlearn.annotation;

import android.os.Bundle;
import android.support.annotation.AnyRes;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by zfz on 2018/2/3.
 */

public class AnnotationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        ButterKnife.bind(this);
        helloWorld(null);
        sayHello(null);

        //setLayout(R.string.app_name);
        //setLayout(R.layout.add_layout);

        //setColor(R.layout.add_layout);
        //setColor(R.color.gold);

        setAnyRes(R.layout.add_layout);
        setAnyRes(R.color.colorAccent);

        //setStringRes("app_name");
        setStringRes(R.string.app_name);

        learnAnnotation();
        learnRangeAnnotation();
    }

    /**
     * 学习@IntDef 注解
     *
     * @author Zhang Fengzhou
     * create at 2018/2/3 15:06
     */
    private void learnAnnotation() {
        ActionBarImpl actionBar = new ActionBarImpl();
        actionBar.setNavigationMode(AbstractActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setNavigationMode(AbstractActionBar.NAVIGATION_MODE_STANDARD | AbstractActionBar.NAVIGATION_MODE_ABS);
        System.out.println("ActionBar navigationMode " + actionBar.getNavigationMode());

        actionBar.setNavigationActionBarMode(AbstractActionBar.NAVIGATION_MODE_ABS);
        //actionBar.setNavigationActionBarMode(AbstractActionBar.NAVIGATION_MODE_STANDARD | AbstractActionBar.NAVIGATION_MODE_ABS);

        System.out.println("ActionBar navigationActionBarMode " + actionBar.getNavigationActionBarMode());
    }

    /**
     * 学习值范围注解
     *
     * @author Zhang Fengzhou
     * create at 2018/2/3 15:05
     */
    private void learnRangeAnnotation() {
        setStudentsList(new ArrayList<>());

        //setMaxNameString("wobushilalalalal");
        setMaxNameString("1000");

        //setMaxArrayElements(new String[]{"xiaohong"});
        setMaxArrayElements(new String[]{"xiaohong", "xiaoming"});

        //setAlpha(256);
        setAlpha(255);

        //setRatio(2.0f);
        setRatio(0.4f);
    }

    private void setStudentsList(@Size(min = 1) List<String> students) {

    }

    // 表示设置的字符串的字符个数最大是10
    private void setMaxNameString(@Size(max = 10) String namea) {

    }

    // 设置数组的元素个数是2个
    private void setMaxArrayElements(@Size(2) String[] names) {

    }

    // 设置传入的参数alpha最小是0 最大是255
    private void setAlpha(@IntRange(from = 0, to = 255) int alha) {

    }

    // 设置输入的参数ratio最小时0 最大是1.0f
    private void setRatio(@FloatRange(from = 0.0f, to = 1.0f) float ratio) {

    }


    private void helloWorld(@NonNull String name) {
        System.out.println("Hello World " + name);
    }

    private void sayHello(@Nullable String name) {
        System.out.println("hello " + name);
    }

    private void setLayout(@LayoutRes int layoutResID) {

    }

    /**
     * @Desc :
     * @Params :
     * @Author : Zhang Fengzhou
     * create at :2018/2/3 15:12
     */
    private void setColor(@ColorRes int colorID) {

    }

    private void setBoolean(@BoolRes int isShow) {

    }

    private void setAnyRes(@AnyRes int anyRes) {

    }

    private void setStringRes(@StringRes int stringRes) {

    }
}
