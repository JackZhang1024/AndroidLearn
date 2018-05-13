package com.lucky.androidlearn.annotation;

import android.annotation.TargetApi;
import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zfz on 2018/2/3.
 */

public abstract class AbstractActionBar {

    // 告知编译器不要在.class文件中存储注解数据
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NAVIGATION_MODE_STANDARD, NAVIGATION_MODE_LIST, NAVIGATION_MODE_ABS})
    public @interface NavigationMode{

    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag = true, value = {NAVIGATION_MODE_STANDARD, NAVIGATION_MODE_LIST, NAVIGATION_MODE_ABS})
    public @interface NavigationActionBarMode{

    }


    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.FIELD)
    public @interface NavigationTitle{
        String value() default "girls";
    }

    // 定义常量
    public static final int NAVIGATION_MODE_STANDARD = 0;
    public static final int NAVIGATION_MODE_LIST = 1;
    public static final int NAVIGATION_MODE_ABS = 2;

    @NavigationMode
    public abstract int getNavigationMode();

    public abstract void setNavigationMode(@NavigationMode int mode);

    @NavigationActionBarMode
    public abstract int getNavigationActionBarMode();

    public abstract void setNavigationActionBarMode(@NavigationActionBarMode int mode);



}
