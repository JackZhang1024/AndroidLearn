package com.lukcyboy.simpleaar;

public class SimpleAarLog {

    public static boolean DEBUG = false;

    private static SimpleAarLog mSimpleArrLog;

    public static SimpleAarLog getInstance() {
        if (mSimpleArrLog == null) {
            mSimpleArrLog = new SimpleAarLog();
        }
        return mSimpleArrLog;
    }

}
