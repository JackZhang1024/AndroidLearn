package com.lucky.androidlearn.ipc.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

// TCP 服务端
public class TCPServerService extends Service {

    private boolean mIsServiceDestroyed = false;
    private String[] mDefinedMessages = new String[]{
            "你好啊，哈哈",
            "请问你叫什么名字啊",
            "北京的天气不错啊",
            "你知道吗?我可以和多个人同时聊天的哦",
            "哈哈，我给你讲个笑话吧，从前有个庙，庙里有个和尚，和尚说..."
    };


    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed = true;
        super.onDestroy();
    }

    private class TcpServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            // 监听8688端口
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                System.err.println("Establish tcp server failed, port： 8688");
                e.printStackTrace();
                return;
            }
            while (!mIsServiceDestroyed) {
                try {
                    System.out.println("ready for connect...");
                    Socket client = serverSocket.accept();
                    System.out.println("accept connect success");
                    // 每建立一个连接 就起一个线程 进行通信
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException {
        // 用于接受客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // 用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("欢迎来到聊天室!");
        while (!mIsServiceDestroyed) {
            String str = in.readLine();
            System.out.println("msg from client : " + str);
            if (str == null) {
                // 客户端断开连接 怎么判断  如果客户端连接断开后  socket输入流返回的数据是null
                break;
            }
            int index = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[index];
            out.println(msg);
            System.out.println("send: " + msg);
        }
        System.out.println("client quit");
        // 关闭流
        if (out != null) {
            out.close();
        }
        if (in != null) {
            in.close();
        }
        client.close();
    }


}
