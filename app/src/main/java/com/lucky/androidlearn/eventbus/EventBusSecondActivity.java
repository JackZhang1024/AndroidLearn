package com.lucky.androidlearn.eventbus;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventBusSecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus_second);
        ButterKnife.bind(this);
        registerEventListener();
    }


    private void registerEventListener() {
        EventContainer container = new EventContainer();
        EventBus.getDefault().register(container);
    }

}
