package com.lucky.androidlearn.multithread;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 条件变量
 */
public class ConditionTestCase {
    private static final String TAG = "ConditionTestCase";
    private MyAccount myAccount = new MyAccount("12345678", 100);

    public void doTest() {
        ExecutorService threadPool = Executors.newFixedThreadPool(2); // 把 nThread改成2试试
        threadPool.execute(new SaveThread("张一", myAccount, 100));
        threadPool.execute(new SaveThread("张二", myAccount, 200));
        threadPool.execute(new WithDrawThread("王一", myAccount, 30));
        threadPool.execute(new SaveThread("张三", myAccount, 300));
        threadPool.execute(new WithDrawThread("王二", myAccount, 40));
        threadPool.execute(new WithDrawThread("王三", myAccount, 40));
        threadPool.execute(new SaveThread("张四", myAccount, 400));
        threadPool.shutdown();
    }

    // 存款线程
    public class SaveThread extends Thread {
        private String name;
        private int x;
        private MyAccount myAccount;

        public SaveThread(String name, MyAccount myAccount, int x) {
            this.name = name;
            this.x = x;
            this.myAccount = myAccount;
        }

        @Override
        public void run() {
            super.run();
            myAccount.saving(x, name);
        }
    }

    // 取款线程
    public class WithDrawThread extends Thread {

        private String name;
        private int x;
        private MyAccount myAccount;

        public WithDrawThread(String name, MyAccount myAccount, int x) {
            this.name = name;
            this.x = x;
            this.myAccount = myAccount;
        }

        @Override
        public void run() {
            super.run();
            myAccount.withDraw(x, name);
        }
    }


    // 资金账户
    public class MyAccount {
        // 账户ID
        private String accountID;
        // 账户资金
        private int cash;
        private ReentrantLock lock = new ReentrantLock();
        // 存款条件
        private Condition saveCondition = lock.newCondition();
        // 取款条件
        private Condition widthDrawCondition = lock.newCondition();

        public MyAccount(String accountID, int cash) {
            this.accountID = accountID;
            this.cash = cash;
        }

        // 存款
        public void saving(int x, String name) {
            lock.lock();
            if (x > 0) {
                // 存入款大于0 则进行加操作
                cash += x;
                Log.e(TAG, "saving: " + name + " 存款 " + x + " 存款余额为 " + cash);
            }
            widthDrawCondition.signalAll();
            lock.unlock();
        }

        // 取款
        public void withDraw(int x, String name) {
            lock.lock();
            try {
                while (cash - x < 0) { // 如果存款小于0 则等待
                    widthDrawCondition.await();
                }
                cash -= x;
                Log.e(TAG, "withDraw: " + name + " 取款 " + x + " 存款余额为 " + cash);
                saveCondition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    }


}
