package com.lucky.androidlearn.eventbus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventBusActivity extends AppCompatActivity {

    private static final String TAG = "EventBusActivity";
    private EventContainer mEventContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        ButterKnife.bind(this);
        mEventContainer = new EventContainer();
        registerEventListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventListener();
    }

    @OnClick(R.id.btn_send)
    public void onSendMessageClick() {
        new Thread(()->{
            EventContainer.Event event = new EventContainer.Event();
            event.setCode(100);
            event.setMsg("我来自工作线程");
            EventBus.getDefault().post(event);
        }).start();
    }

    @OnClick(R.id.btn_send_sticky)
    public void onSendStickyMessageClick(){
        EventContainer.StickyEvent event = new EventContainer.StickyEvent();
        event.setCode(101);
        event.setMsg("我来主线程，我是粘滞消息");
        EventBus.getDefault().postSticky(event);
        startActivity(new Intent(this, EventBusSecondActivity.class));
    }

    private void registerEventListener() {
        EventBus.getDefault().register(mEventContainer);
    }

    private void unregisterEventListener(){
        EventBus.getDefault().unregister(mEventContainer);
    }


}
