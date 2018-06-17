package com.lukcyboy.simpleaar2;

public class SimpleAar2Log {

    private static SimpleAar2Log mSimpleAar2Log;

    public static SimpleAar2Log getInstance() {
        if (mSimpleAar2Log == null) {
            mSimpleAar2Log = new SimpleAar2Log();
        }
        return mSimpleAar2Log;
    }
}
