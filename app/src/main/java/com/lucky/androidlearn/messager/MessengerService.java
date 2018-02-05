package com.lucky.androidlearn.messager;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * 利用Messenger来进行进程间的交互
 */
public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    @SuppressWarnings("handlerLeak")
    class MessengerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.MESSAGE_FROM_CLIENT:
                    Bundle bundle = msg.getData();
                    String message = bundle.getString("msg");
                    Log.e(TAG, "handleMessage: " + message);
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, Constants.MESSAGE_FROM_SERVER);
                    Bundle replyBundle = new Bundle();
                    replyBundle.putString("reply", "嗯, 好的, 你发送过来的消息我收到了，稍后回复！");
                    replyMessage.setData(replyBundle);
                    try {
                        client.send(replyMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
            Log.e(TAG, "handleMessage: ");
        }
    }

    private Messenger messenger = new Messenger(new MessengerHandler());

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }


}
