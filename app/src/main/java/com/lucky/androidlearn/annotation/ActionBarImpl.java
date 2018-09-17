package com.lucky.androidlearn.annotation;

/**
 * Created by zfz on 2018/2/3.
 */

public class ActionBarImpl extends AbstractActionBar {

    @NavigationMode
    private int navigationMode;

    @NavigationActionBarMode
    private int navigationActionBarMode;

    // 当注解只有一个属性的时候 可以将属性或者方法设置成 String value()
    // 这样就不用再使用注解的时候 使用注解元素名称和等号
    @NavigationTitle("Science")
    private String navigatioinTitle;


    @Override
    public int getNavigationMode() {
        return navigationMode;
    }

    @Override
    public void setNavigationMode(@NavigationMode int mode) {
        navigationMode = mode;
    }

    @Override
    public int getNavigationActionBarMode() {
        return navigationActionBarMode;
    }

    @Override
    public void setNavigationActionBarMode(@NavigationActionBarMode int mode) {
        navigationActionBarMode = mode;
    }


    @Override
    public void setNavigationMenuText(@NavigationMenuText String menuText) {

    }
}
