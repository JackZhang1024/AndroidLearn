package com.lucky.androidlearn.multithread;

import android.util.Log;

public class ThreadSafeCheckTestCase {
    private Producer producer = new Producer();
    private static final String TAG = "ThreadSafeCheckTestCase";

    public void doCheck() {
        WorkThreadA workThreadA = new WorkThreadA();
        WorkThreadB workThreadB = new WorkThreadB();
        workThreadA.start();
        workThreadB.start();
    }

    class WorkThreadA extends Thread {

        @Override
        public void run() {
            super.run();
            producer.doProduce();
        }
    }

    class WorkThreadB extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(200);
                producer.doProduce();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class Producer {

        public void doProduce() {
            produceBeer();
            produceBread();
        }

        private synchronized void produceBeer() {
            Log.e(TAG, "produceBeer: " + Thread.currentThread().getName());
        }

        private void produceBread() {
            Log.e(TAG, "produceBread: " + Thread.currentThread().getName());
        }

    }

}
