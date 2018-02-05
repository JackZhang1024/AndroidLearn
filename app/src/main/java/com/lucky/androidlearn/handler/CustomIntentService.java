package com.lucky.androidlearn.handler;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class CustomIntentService extends IntentService {


    public CustomIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
