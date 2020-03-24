package com.lucky.androidlearn.annotation;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.AnyRes;
import androidx.annotation.BoolRes;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by zfz on 2018/2/3.
 */

public class AnnotationActivity extends AppCompatActivity {

    private static final String TAG = "AnnotationActivity";

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
        getJSMethods(JSMethodObject.class, JSMethodObject.class.getName());
        getJSMethods(JSDeclareMethodObject.class, JSDeclareMethodObject.class.getName());
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
        //actionBar.setNavigationMode(9);
        //actionBar.setNavigationMode(AbstractActionBar.NAVIGATION_MODE_STANDARD | AbstractActionBar.NAVIGATION_MODE_ABS);
        System.out.println("ActionBar navigationMode " + actionBar.getNavigationMode());

        actionBar.setNavigationActionBarMode(AbstractActionBar.NAVIGATION_MODE_ABS);
        //actionBar.setNavigationActionBarMode(AbstractActionBar.NAVIGATION_MODE_STANDARD | AbstractActionBar.NAVIGATION_MODE_ABS);

        System.out.println("ActionBar navigationActionBarMode " + actionBar.getNavigationActionBarMode());

        actionBar.setNavigationMenuText(AbstractActionBar.NAVIGATION_MENU_START);
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


    public void getJSMethods(Class<? extends JSObject> clz, String constructorName) {
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            Log.e(TAG, "getJSMethods: MethodName " + method.getName());
            JSMethod jsMethod = (JSMethod) method.getAnnotation(JSMethod.class);
            String jsMethodName = jsMethod.name();
            Log.e(TAG, "getJSMethods: JSMethodName " + jsMethodName);
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class klz : parameterTypes) {
                String parameterType = klz.getName();
                Log.e(TAG, "getJSMethods: ParameterType " + parameterType);
            }
        }
        try {
            //JSObject object = clz.newInstance();
            Constructor<? extends JSObject> constructor = clz.getConstructor(String.class);
            JSObject jsObject = constructor.newInstance(constructorName);
            Log.e(TAG, "getJSMethods: constructorName "+jsObject.getJSObjectName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
