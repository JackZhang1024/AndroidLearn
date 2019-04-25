package com.lucky.androidlearn.ipc.socket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SocketIPCActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_ipc);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_start_socket_server)
    public void onStartSocketServerClick(View view){
        Intent intent = new Intent(this, TCPServerService.class);
        startService(intent);
    }

}
