package com.lucky.androidlearn.rxjava2.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author zfz
 * Created by zfz on 2018/3/17.
 */

public class TaskLauncher<T> {

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    public <T> T execute(Callable<T> callable) throws Exception {
        Future<T> future = executorService.submit(callable);
        return future.get();
    }

    public void executeRunnable(Runnable runnable) {
        executorService.execute(runnable);
    }

    public void submitRunnable(Runnable runnable) throws Exception {
        executorService.submit(runnable);
    }

    public void submitRunnable(Runnable runnable, T t) {
        executorService.submit(runnable, t);
    }


}
