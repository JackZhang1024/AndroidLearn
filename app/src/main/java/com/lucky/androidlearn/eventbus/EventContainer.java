package com.lucky.androidlearn.eventbus;

import android.util.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventContainer {

    private static final String TAG = "EventContainer";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleEvent(Event event) {
        Log.e(TAG, "onHandleEvent: code " + event.getCode() + " msg " + event.getMsg() + " ThreadName " + Thread.currentThread().getName());
    }

    // ThreadMode.MAIN 适合用于处理任务耗时短的操作
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleStickyEvent(StickyEvent event) {
        Log.e(TAG, "onHandleStickyEvent: code " + event.getCode() + " msg " + event.getMsg() + " ThreadName " + Thread.currentThread().getName());
    }



    static class Event {
        private String msg;
        private int code;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    static class StickyEvent {
        private String msg;
        private int code;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }


}
