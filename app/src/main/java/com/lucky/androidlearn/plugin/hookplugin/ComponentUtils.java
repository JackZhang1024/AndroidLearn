package com.lucky.androidlearn.plugin.hookplugin;

import android.content.ComponentName;

public class ComponentUtils {

    public static String componentFullName(ComponentName component){
        return component.getClassName();
    }

}
