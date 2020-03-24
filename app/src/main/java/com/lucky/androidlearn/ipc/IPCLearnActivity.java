package com.lucky.androidlearn.ipc;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.aidl.BookManagerActivity;
import com.lucky.androidlearn.ipc.socket.SocketIPCActivity;
import com.lucky.androidlearn.messager.MessengerActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class IPCLearnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc_learn);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_ipc_share_file)
    public void onIPCShareFileClick(){

    }

    @OnClick(R.id.btn_ipc_messager)
    public void onIPCMessager(){
        Intent intent = new Intent(this, MessengerActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_ipc_aidl)
    public void onIPCAIDL(){
        Intent intent = new Intent(this, BookManagerActivity.class);
        startActivity(intent);
    }


    // socket通讯
    @OnClick(R.id.btn_ipc_socket)
    public void onSocketClick(){
        Intent intent = new Intent(this, SocketIPCActivity.class);
        startActivity(intent);
    }

    // provider通讯
    @OnClick(R.id.btn_ipc_contentprovider)
    public void onContentProviderClick(){

    }


}
