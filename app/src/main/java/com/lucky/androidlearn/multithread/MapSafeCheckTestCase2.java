package com.lucky.androidlearn.multithread;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class MapSafeCheckTestCase2 {
    private static final String TAG = "MapSafeCheckTestCase2";
    private static final Map<String, String> keyValues = new HashMap<>();

    public void doCheckTest() {
        try {
            WorkThreadA workThreadA = new WorkThreadA();
            WorkThreadB workThreadB = new WorkThreadB();
            workThreadA.start();
            workThreadB.start();
            Thread.sleep(1000);
            for(int i =0; i< 50; i++){
                if (!keyValues.get(String.valueOf(i)).equals(String.valueOf(i))){
                    Log.e(TAG, "doCheckTest: "+String.valueOf(i)+" value "+keyValues.get(String.valueOf(i)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class WorkThreadA extends Thread {

        @Override
        public void run() {
            super.run();
            for (int i = 0; i < 25; i++) {
                keyValues.put(String.valueOf(i), String.valueOf(i));
            }
        }
    }

    class WorkThreadB extends Thread {

        @Override
        public void run() {
            super.run();
            for (int i = 25; i < 50; i++) {
                keyValues.put(String.valueOf(i), String.valueOf(i));
            }
        }
    }


}
