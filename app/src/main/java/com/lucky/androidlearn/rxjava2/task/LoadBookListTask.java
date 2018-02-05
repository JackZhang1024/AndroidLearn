package com.lucky.androidlearn.rxjava2.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author zfz
 * Created by zfz on 2018/3/18.
 */

public class LoadBookListTask implements Callable<List<String>> {

    @Override
    public List<String> call() throws Exception {
        Thread.sleep(3000);
        List<String> bookList = new ArrayList<>();
        bookList.add("Java编程思想");
        bookList.add("Java优化指南");
        bookList.add("Java虚拟机");
        return bookList;
    }
}
