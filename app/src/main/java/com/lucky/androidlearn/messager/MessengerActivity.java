package com.lucky.androidlearn.messager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;

public class MessengerActivity extends AppCompatActivity {

    private static final String TAG = "MessengerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc_messager);
        ButterKnife.bind(this);
        bindMessengerService();
    }

    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    @SuppressWarnings("handlerLeak")
    class MessengerHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.MESSAGE_FROM_SERVER:
                    String messgae = msg.getData().getString("reply");
                    Log.e(TAG, "message from server : "+messgae);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: ");
            Messenger messenger = new Messenger(service);
            Message message = Message.obtain(null, Constants.MESSAGE_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "This is message from Client");
            message.setData(bundle);
            try {
                message.replyTo = mGetReplyMessenger;
                messenger.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: ");
        }
    };

    private void bindMessengerService() {
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceConnection!=null){
            unbindService(mServiceConnection);
        }
    }
}
