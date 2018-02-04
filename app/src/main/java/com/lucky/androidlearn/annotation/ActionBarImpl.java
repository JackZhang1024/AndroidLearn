package com.lucky.androidlearn.annotation;

/**
 * Created by zfz on 2018/2/3.
 */

public class ActionBarImpl extends AbstractActionBar {

    @NavigationMode
    private int navigationMode;

    @NavigationActionBarMode
    private int navigationActionBarMode;

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
}
