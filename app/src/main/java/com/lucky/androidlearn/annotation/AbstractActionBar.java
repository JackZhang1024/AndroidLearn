package com.lucky.androidlearn.annotation;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *  @Retention(SOURCE)
 *  @StringDef({
 *     POWER_SERVICE,
 *     WINDOW_SERVICE,
 *     LAYOUT_INFLATER_SERVICE
 *  })
 *  public @interface ServiceName {}
 *  public static final String POWER_SERVICE = "power";
 *  public static final String WINDOW_SERVICE = "window";
 *  public static final String LAYOUT_INFLATER_SERVICE = "layout_inflater";
 *  ...
 *  public abstract Object getSystemService(@ServiceName String name);
 *
 *
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
    @StringDef
    public @interface NavigationActionBarMode{

    }


    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.FIELD)
    public @interface NavigationTitle{
        String value() default "girls";
    }

    // 对传入的字符串进行限制
    @StringDef({NAVIGATION_MENU_START, NAVIGATION_MENU_PAUSE, NAVIGATION_MENU_STOP})
    @Retention(RetentionPolicy.CLASS)
    public @interface NavigationMenuText{

    }

    public static final String NAVIGATION_MENU_START = "start";
    public static final String NAVIGATION_MENU_PAUSE = "pause";
    public static final String NAVIGATION_MENU_STOP = "stop";


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

    public abstract void setNavigationMenuText(@NavigationMenuText String menuText);



}
