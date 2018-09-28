package com.lucky.androidlearn.security;

import java.io.File;

// 判断手机是否被root过
public class MobileRoot {

    /**
     * 判断手机是否被Root
     */
    public static boolean isRoot() {
        if (new File("/system/bin/su").exists() || new File("/system/xbin/su").exists()) {
            return true;
        }
        return false;
    }

}
