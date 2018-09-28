package com.lucky.androidlearn.multithread;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 进行Map线程安全的检查
public class MapSafeCheckTestCase {
    private static final String TAG = "MapSafeCheckTestCase";
    public static Map<String, String> keyValues = new HashMap<String, String>(4, 0.25f);
    private List<Thread> mThreadList = new ArrayList<>();

    public MapSafeCheckTestCase() {
        keyValues.clear();
        keyValues.put("A", "Hello");
        keyValues.put("B", "World");
        mThreadList.add(new WorkThreadA("A1"));
        mThreadList.add(new WorkThreadA("A2"));
        mThreadList.add(new WorkThreadA("A3"));
        mThreadList.add(new WorkThreadA("A4"));
        mThreadList.add(new WorkThreadA("A5"));
        mThreadList.add(new WorkThreadB("B1"));
        mThreadList.add(new WorkThreadB("B2"));
        mThreadList.add(new WorkThreadB("B3"));
        mThreadList.add(new WorkThreadB("B4"));
        mThreadList.add(new WorkThreadB("B5"));
        mThreadList.add(new WorkThreadC("C1"));
        mThreadList.add(new WorkThreadC("C2"));
        mThreadList.add(new WorkThreadC("C3"));
        mThreadList.add(new WorkThreadC("C4"));
    }

    public void doTest() {
        for (int i = 0; i < mThreadList.size(); i++) {
            mThreadList.get(i).start();
        }
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 进行put操作 put操作之后 可能对后面的get操作造成取值为null的情况
    public class WorkThreadC extends Thread {

        private String mThreadName;

        public WorkThreadC(String name) {
            this.mThreadName = name;
        }


        @Override
        public void run() {
            super.run();
            try {
                for (int i = 0; i < 1000; i++) {
                    if (i % 2 == 0) {
                        keyValues.put("A", "Hello");
                    } else {
                        keyValues.put("B", "World");
                    }
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public class WorkThreadA extends Thread {
        private String mThreadName;

        public WorkThreadA(String name) {
            this.mThreadName = name;
        }

        @Override
        public void run() {
            super.run();
            // 执行100次取Key为A对应的Value
            try {
                for (int i = 0; i < 1000; i++) {
                    String value = keyValues.get("A");
                    if (!"Hello".equals(value)) {
                        Log.e(TAG, mThreadName + " run: Something Wrong " + value);
                        break;
                    } else if (TextUtils.isEmpty(value)) {
                        Log.e(TAG, mThreadName + " run: Something Empty " + value);
                        break;
                    }
                    Log.e(TAG, mThreadName + " run: " + value);
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public class WorkThreadB extends Thread {
        private String mThreadName;

        public WorkThreadB(String name) {
            this.mThreadName = name;
        }

        @Override
        public void run() {
            super.run();
            // 执行100次取Key为B对应的Value
            try {
                for (int i = 0; i < 1000; i++) {
                    String value = keyValues.get("B");
                    if (!"World".equals(value)) {
                        Log.e(TAG, mThreadName + " run: Something Wrong " + value);
                        break;
                    } else if (TextUtils.isEmpty(value)) {
                        Log.e(TAG, mThreadName + " run: Something Empty " + value);
                        break;
                    }
                    Log.e(TAG, mThreadName + " run: " + value);
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
