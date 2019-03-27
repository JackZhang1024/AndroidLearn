package com.lucky.androidlearn.lru;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class LRUActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lruTest();
    }


    private void lruTest(){
        LRUCache lruCache = new LRUCache(5);
        lruCache.put("001", "用户1信息");
        lruCache.put("002", "用户2信息");
        lruCache.put("003", "用户3信息");
        lruCache.put("004", "用户4信息");
        lruCache.put("005", "用户5信息");


        lruCache.get("002");
        lruCache.put("004", "用户2信息更改");
        lruCache.put("006", "用户6信息");
        System.out.println("  "+lruCache.get("001"));
        System.out.println("  "+lruCache.get("006"));
    }


}
