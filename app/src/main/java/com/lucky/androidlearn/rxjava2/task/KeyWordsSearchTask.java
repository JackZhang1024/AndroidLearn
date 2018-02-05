package com.lucky.androidlearn.rxjava2.task;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class KeyWordsSearchTask implements Callable<List<String>> {
    private static final String TAG = "KeyWordsSearchTask";
    private String keyWords;
    private String[] keyWordList = new String[]{ "A1", "AB", "ABB", "AA", "AC", "A2", "AB2", "ABB2", "AA2", "AC2"};

    public KeyWordsSearchTask(String keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public List<String> call() throws Exception {
        Log.e(TAG, "call: "+Thread.currentThread().getName());
        Thread.sleep(3000);
        List<String> results = new ArrayList<>();
        for (String str: keyWordList){
             if (str.contains(keyWords)){
                 results.add(str);
             }
        }
        return results;
    }
}
