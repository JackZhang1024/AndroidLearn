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
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessengerActivity extends AppCompatActivity {

    private static final String TAG = "MessengerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc_messager);
        ButterKnife.bind(this);
        bindMessengerService();
    }

    @BindView(R.id.et_input)
    EditText mEtData;

    @OnClick(R.id.btn_send)
    public void onSendMessage() {
        try {
            String data = mEtData.getText().toString();
            //System.out.println("data "+data);
            Message message = Message.obtain(null, Constants.MESSAGE_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg", data);
            message.setData(bundle);
            message.replyTo = mGetReplyMessenger;
            mSendMessenger.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 负责接收服务端发送过来的消息的Messenger
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());
    // 负责发送消息到服务端的Messenger
    private Messenger mSendMessenger;

    @SuppressWarnings("handlerLeak")
    class MessengerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_FROM_SERVER:
                    String messgae = msg.getData().getString("reply");
                    Log.e(TAG, "message from server : " + messgae);
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
            //Messenger messenger = new Messenger(service);
            mSendMessenger = new Messenger(service);
            Message message = Message.obtain(null, Constants.MESSAGE_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "This is message from Client");
            message.setData(bundle);
            try {
                message.replyTo = mGetReplyMessenger;
                mSendMessenger.send(message);
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
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
    }
}
