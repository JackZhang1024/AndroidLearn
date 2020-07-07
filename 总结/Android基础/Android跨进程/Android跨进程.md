- [Android跨进程](#android跨进程)
  - [文件](#文件)
  - [Socket](#socket)
  - [Messager](#messager)
  - [AIDL](#aidl)
    - [1. 创建AIDL](#1-创建aidl)
    - [2. 创建服务端](#2-创建服务端)
    - [3. 创建客户端](#3-创建客户端)
  - [ContentProvider](#contentprovider)
## Android跨进程

Android跨进程通信的方式有文件，Socket, Messager, AIDL, ContentProvider。文件因为存在同时读写的问题，所以比较适合对实时性要求不高的应用场景，Socket本来最常见的是跨机器间通信，但是也可以用于不同进程的通信，但是存在安全问题，需要校验身份的时候，比较麻烦，AIDL和Messager其实是同一种方式，利用Binder机制来实现跨进程通信，比较安全，是Android区别于Linux的跨进程方式，ContentProvider也是一种跨进程的通信方式，但是一般用于不同的应用之间的数据库访问。

### 文件
文件就是不同进程之间进行普通的文件读写，但是有延迟性和同时读写的问题，不可靠。
```java
 try {
      // 这块只能模拟，正常的情况下是访问的是可以共同访问的文件，而不是应用程序私有的目录
      File shareFile = new File(getDataDir(), "share.txt");
      FileOutputStream fos = new FileOutputStream(shareFile);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
      objectOutputStream.writeObject("我是共享文件");

      FileInputStream fis = new FileInputStream(shareFile);
      ObjectInputStream objectInputStream = new ObjectInputStream(fis);
      Object object = objectInputStream.readObject();
      System.out.println("object "+object.toString());
} catch (Exception e) {
    e.printStackTrace();
}
```
### Socket
服务端代码
```java
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

// 启动服务
Intent intent = new Intent(this, TCPServerService.class);
startService(intent);
```

客户端代码
```java
/*客户端代码*/
public class SocketIPClientActivity extends AppCompatActivity {

    private Socket mClientSocket;
    private PrintWriter mPrintWriter;
    @BindView(R.id.et_input)
    EditText mEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_client);
        ButterKnife.bind(this);

        new Thread() {
            @Override
            public void run() {
                super.run();
                connectTCPServer();
            }
        }.start();
    }

    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("127.0.0.1", 8688);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                System.out.println("connect server success!");
                mPrintWriter.println("我是来自客户端的......");

            } catch (IOException e) {
                SystemClock.sleep(1000 * 3);
                //e.printStackTrace();
                System.out.println("Connect tcp server failed, retry...");
            }
        }
        try {
            // 接受服务端的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!SocketIPClientActivity.this.isFinishing()) {
                String msg = br.readLine();
                if (msg != null) {
                    System.out.println("msg from server " + msg);
                }
                SystemClock.sleep(3000);
                //mPrintWriter.println("很高兴认识你...");
            }
            System.out.println("quit...");
            mPrintWriter.close();
            br.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @OnClick(R.id.btn_send)
    public void onSendMsgClick() {
        String msg = mEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(msg)) {
            new Thread(new MyPrintTask(msg)).start();
        } else {
            Toast.makeText(this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    class MyPrintTask implements Runnable {
        private String msg;
        public MyPrintTask(String msg){
            this.msg = msg;
        }

        @Override
        public void run() {
            mPrintWriter.println(msg);
        }
    }
}
```
先启动服务端进程，然后再开启客户端进程，然后客户端从输入框中输入文本内容，服务端接收到客户端发过来的消息，同时也会发回给客户端消息。
输出结果：

服务端进程输出
```java
2020-07-07 09:47:45.229 9470-9542/com.lucky.androidlearn I/System.out: ready for connect...
2020-07-07 09:47:55.945 9470-9542/com.lucky.androidlearn I/System.out: accept connect success
2020-07-07 09:47:55.945 9470-9542/com.lucky.androidlearn I/System.out: ready for connect...
2020-07-07 09:47:55.953 9470-9561/com.lucky.androidlearn I/System.out: msg from client : 我是来自客户端的......
2020-07-07 09:47:55.953 9470-9561/com.lucky.androidlearn I/System.out: send: 你好啊，哈哈
2020-07-07 09:50:48.178 9470-9561/com.lucky.androidlearn I/System.out: msg from client : uuuuu
2020-07-07 09:50:48.179 9470-9561/com.lucky.androidlearn I/System.out: send: 北京的天气不错啊
```
客户端进程输出
```java
2020-07-07 09:47:55.946 9543-9560/com.lucky.customviewlearn I/System.out: connect server success!
2020-07-07 09:47:55.953 9543-9560/com.lucky.customviewlearn I/System.out: msg from server 欢迎来到聊天室!
2020-07-07 09:47:58.955 9543-9560/com.lucky.customviewlearn I/System.out: msg from server 你好啊，哈哈
2020-07-07 09:50:48.179 9543-9560/com.lucky.customviewlearn I/System.out: msg from server 北京的天气不错啊
```

### Messager
代码中的常量
```java
public class Constants {
    /**
     * 客户端发送到服务端
     * */
    public static final int MESSAGE_FROM_CLIENT = 100;

    /**
     * 服务端发送到客户端
     * */
    public static final int MESSAGE_FROM_SERVER = 101;
}
```

服务端代码
```java
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
```
在清单文件中进行注册
```java
<!-- Messenger 的使用 -->
<service
    android:name=".messager.MessengerService"
    android:enabled="true"
    android:exported="false"
    android:process=":remote_ms" 
/>
```

客户端代码

```java
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
                // Message的replyTo字段是处理发送出去的消息之后响应对方返回的消息处理机制
                // 相当于一个回调机制 当对方有返回消息的时候 消息会回传到Messenger的构造参数Handler中的
                // handleMessage方法中
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
```
输出结果：

客户端输出
```java
2020-07-07 10:28:55.327 11295-11295/com.lucky.androidlearn E/MessengerActivity: onServiceConnected: 
2020-07-07 10:28:55.352 11295-11295/com.lucky.androidlearn E/MessengerActivity: message from server : 嗯, 好的, 你发送过来的消息我收到了，稍后回复！
2020-07-07 10:29:29.761 11295-11295/com.lucky.androidlearn E/MessengerActivity: message from server : 嗯, 好的, 你发送过来的消息我收到了，稍后回复！
2020-07-07 10:29:34.200 11295-11295/com.lucky.androidlearn E/MessengerActivity: message from server : 嗯, 好的, 你发送过来的消息我收到了，稍后回复！
2020-07-07 10:29:47.488 11295-11295/com.lucky.androidlearn E/MessengerActivity: message from server : 嗯, 好的, 你发送过来的消息我收到了，稍后回复！
2020-07-07 10:29:49.289 11295-11295/com.lucky.androidlearn E/MessengerActivity: message from server : 嗯, 好的, 你发送过来的消息我收到了，稍后回复！
```
服务端输出
```java
2020-07-07 10:28:55.328 11363-11363/com.lucky.androidlearn E/MessengerService: handleMessage: This is message from Client
2020-07-07 10:28:55.328 11363-11363/com.lucky.androidlearn E/MessengerService: handleMessage: 
2020-07-07 10:29:29.759 11363-11363/com.lucky.androidlearn E/MessengerService: handleMessage: 11111 
2020-07-07 10:29:29.759 11363-11363/com.lucky.androidlearn E/MessengerService: handleMessage: 
2020-07-07 10:29:34.192 11363-11363/com.lucky.androidlearn E/MessengerService: handleMessage: oooo
2020-07-07 10:29:34.192 11363-11363/com.lucky.androidlearn E/MessengerService: handleMessage: 
2020-07-07 10:29:47.480 11363-11363/com.lucky.androidlearn E/MessengerService: handleMessage: ooooq1qw1
2020-07-07 10:29:47.480 11363-11363/com.lucky.androidlearn E/MessengerService: handleMessage: 
2020-07-07 10:29:49.288 11363-11363/com.lucky.androidlearn E/MessengerService: handleMessage: ooooq1qw1
2020-07-07 10:29:49.288 11363-11363/com.lucky.androidlearn E/MessengerService: handleMessage: 
```
总结：
Messenger是对AIDL的一层包装，对应的AIDL接口是IMessenger。

### AIDL 
AIDL是Android里最常见的跨进程通信方式，Android的AMS, WMS都涉及到了AIDL，通过AIDL，就可以实现客户端与系统服务进行一系列的交互（即系统方法的调用）。AIDL的实现一般有三个步骤，1. 创建AIDL接口 2. 创建服务端 3. 创建客户端
#### 1. 创建AIDL
首先在main的目录下创建一个一个aidl目录，创建相应的包，com.lucky.androidlearn.aidl，同时在java目录下也创建相同的包名。
在java目录下的com.lucky.androidlearn.aidl包中创建Book实体类，该类要实现Parcelable接口。

Book.java
```java
public class Book implements Parcelable {

    private int bookId;
    private String bookName;

    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(bookId);
        out.writeString(bookName);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };


    private Book(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }

    @Override
    public String toString() {
        return String.format("bookId:%s, bookName:%s", bookId, bookName);
    }
}
```

在aidl目录下创建Book.aidl和IBookManager.aidl文件 

Book.aidl
```java
// Book.aidl
package com.lucky.androidlearn.aidl;

parcelable Book;
```

IBookManager.aidl

```java
package com.lucky.androidlearn.aidl;

import com.lucky.androidlearn.aidl.Book;
// Declare any non-default types here with import statements

// AIDL的接口
interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> getBookList();

    void addBook(in Book book);
}
```

点击Build->Rebuild Project，可以自动在app/build/generated/下生成IBookManager.java文件

```java
package com.lucky.androidlearn.aidl;

public interface IBookManager extends android.os.IInterface {
  /** Default implementation for IBookManager. */
  public static class Default implements com.lucky.androidlearn.aidl.IBookManager{

    @Override public java.util.List<com.lucky.androidlearn.aidl.Book> getBookList() throws android.os.RemoteException{
      return null;
    }

    @Override public void addBook(com.lucky.androidlearn.aidl.Book book) throws android.os.RemoteException{
    
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.lucky.androidlearn.aidl.IBookManager
  {
    private static final java.lang.String DESCRIPTOR = "com.lucky.androidlearn.aidl.IBookManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.lucky.androidlearn.aidl.IBookManager interface,
     * generating a proxy if needed.
     */
    public static com.lucky.androidlearn.aidl.IBookManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.lucky.androidlearn.aidl.IBookManager))) {
        return ((com.lucky.androidlearn.aidl.IBookManager)iin);
      }
      return new com.lucky.androidlearn.aidl.IBookManager.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_getBookList:
        {
          data.enforceInterface(descriptor);
          java.util.List<com.lucky.androidlearn.aidl.Book> _result = this.getBookList();
          reply.writeNoException();
          reply.writeTypedList(_result);
          return true;
        }
        case TRANSACTION_addBook:
        {
          data.enforceInterface(descriptor);
          com.lucky.androidlearn.aidl.Book _arg0;
          if ((0!=data.readInt())) {
            _arg0 = com.lucky.androidlearn.aidl.Book.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.addBook(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }

    private static class Proxy implements com.lucky.androidlearn.aidl.IBookManager{
      private android.os.IBinder mRemote;

      Proxy(android.os.IBinder remote){
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder(){
        return mRemote;
      }

      public java.lang.String getInterfaceDescriptor(){
        return DESCRIPTOR;
      }

      /**
      * Demonstrates some basic types that you can use as parameters
      * and return values in AIDL.
      */
      @Override public java.util.List<com.lucky.androidlearn.aidl.Book> getBookList() throws android.os.RemoteException{
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<com.lucky.androidlearn.aidl.Book> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getBookList, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getBookList();
          }
          _reply.readException();
          _result = _reply.createTypedArrayList(com.lucky.androidlearn.aidl.Book.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void addBook(com.lucky.androidlearn.aidl.Book book) throws android.os.RemoteException{
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((book!=null)) {
            _data.writeInt(1);
            book.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().addBook(book);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static com.lucky.androidlearn.aidl.IBookManager sDefaultImpl;
    }
    static final int TRANSACTION_getBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(com.lucky.androidlearn.aidl.IBookManager impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.lucky.androidlearn.aidl.IBookManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }

  /**
  * Demonstrates some basic types that you can use as parameters
  * and return values in AIDL.
  */
  public java.util.List<com.lucky.androidlearn.aidl.Book> getBookList() throws android.os.RemoteException;
  public void addBook(com.lucky.androidlearn.aidl.Book book) throws android.os.RemoteException;

}
```

#### 2. 创建服务端
```java
public class BookManagerService extends Service {

    private static final String TAG = "BMS";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Java实战"));
        mBookList.add(new Book(2, "Python基础学习"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
  
}
```
在清单文件中进行注册，并通过设置android:process=":remote_bms" 开启另外一个进程
```java
<!-- AIDL的使用 -->
<service
    android:name=".aidl.BookManagerService"
    android:enabled="true"
    android:exported="true"
    android:process=":remote_bms">
    <intent-filter>
        <action android:name="com.lucky.aidl.bms" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</service>
```

#### 3. 创建客户端
```java
public class BookManagerActivity extends AppCompatActivity {

    private static final String TAG = "BookManagerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_bms);
        bindBookManagerService();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: ");
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                // 添加书本
                bookManager.addBook(new Book(3, "JavaScript学习"));
                bookManager.addBook(new Book(4, "Linux学习"));
                List<Book> list = bookManager.getBookList();
                Log.e(TAG, "图书列表类型 : "+list.getClass().getCanonicalName());
                Log.e(TAG, "服务端返回的图书列表 : " + list.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: ");
        }

        @Override
        public void onBindingDied(ComponentName name) {

        }
    };

    private void bindBookManagerService() {
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
        stopService(new Intent(this, BookManagerService.class));
    }
}
```

输出结果：
```java
2020-07-07 11:09:16.143 11663-11663/com.lucky.androidlearn E/BookManagerActivity: onServiceConnected: 
2020-07-07 11:09:16.143 11663-11663/com.lucky.androidlearn E/BookManagerActivity: 图书列表类型 : java.util.ArrayList
2020-07-07 11:09:16.143 11663-11663/com.lucky.androidlearn E/BookManagerActivity: 服务端返回的图书列表 : [bookId:1, bookName:Java实战, bookId:2, bookName:Python基础学习, bookId:3, bookName:JavaScript学习, bookId:4, bookName:Linux学习]
```

### ContentProvider

```java
package com.lucky.androidlearn.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BooksProvider extends ContentProvider {
    private DBHelper dbHelper;
    private static final String AUTHORITIES = "com.lucky.androidlearn.provider";
    private static final int BOOK_DIR = 0;  // 处理整个book表
    private static final int BOOK_ITEM = 1; // 处理book表中的某条数据
    private static final int CATEGORY = 2;  // 处理整个category表
    private static final int CATEGORY_ITEM = 3; // 处理category表中的某条数据

    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITIES, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITIES, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITIES, "category", CATEGORY);
        uriMatcher.addURI(AUTHORITIES, "category/#", CATEGORY_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                cursor = db.query("book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                String bookID = uri.getPathSegments().get(1);
                cursor = db.query("book", projection, "id=?", new String[]{bookID}, null, null, sortOrder);
                break;
            case CATEGORY:
                cursor = db.query("category", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryID = uri.getPathSegments().get(1);
                cursor = db.query("category", projection, "id=?", new String[]{categoryID}, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookID = db.insert("book", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITIES + "/book/" + newBookID);
                break;
            case CATEGORY:
            case CATEGORY_ITEM:
                long newCategoryID = db.insert("book", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITIES + "/category/" + newCategoryID);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int deletedRows = -1;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deletedRows = db.delete("book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookID = uri.getPathSegments().get(1);
                deletedRows = db.delete("book", "id=?", new String[]{bookID});
                break;
            case CATEGORY:
                deletedRows = db.delete("category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryID = uri.getPathSegments().get(1);
                deletedRows = db.delete("category", "id=?", new String[]{categoryID});
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int updatedRows = -1;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                updatedRows = db.update("book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookID = uri.getPathSegments().get(1);
                updatedRows = db.update("book", values, "id=?", new String[]{bookID});
                break;
            case CATEGORY:
                updatedRows = db.update("category", values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryID = uri.getPathSegments().get(1);
                updatedRows = db.update("category", values, "id=?", new String[]{categoryID});
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRows;
    }

    /**
     * 1. 必须以vnd 开头。
     * 2. 如果内容URI 以路径结尾，则后接android.cursor.dir/，如果内容URI 以id 结尾，
     * 则后接android.cursor.item/。
     * 3. 最后接上vnd.<authority>.<path>。
     * 所以，对于content://com.example.app.provider/table1 这个内容URI，它所对应的MIME
     * 类型就可以写成：
     * vnd.android.cursor.dir/vnd.com.example.app.provider.table1
     * 对于content://com.example.app.provider/table1/1 这个内容URI，它所对应的MIME 类型
     * 就可以写成：
     * vnd.android.cursor.item/vnd. com.example.app.provider.table1
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String type = "";
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                type = "vnd.android.cursor.dir/vnd.com.lucky.androidlearn.provider.book";
                break;
            case BOOK_ITEM:
                type = "vnd.android.cursor.item/vnd.com.lucky.androidlearn.provider.book";
                break;
            case CATEGORY:
                type = "vnd.android.cursor.dir/vnd.com.lucky.androidlearn.provider.category";
                break;
            case CATEGORY_ITEM:
                type = "vnd.android.cursor.item/vnd.com.lucky.androidlearn.provider.category";
                break;
        }
        return null;
    }
}

```
在清单文件中注册ContentProvider
```java
<provider
    android:name="com.lucky.androidlearn.provider.BooksProvider"
    android:authorities="com.lucky.androidlearn.provider"
    android:exported="true"
    >
</provider>
```

客户端代码 

可以在其他应用进程中对ContentProvider进行增加，修改，查询，删除操作。

```java
// 插入书
Uri uri = Uri.parse("content://com.lucky.androidlearn.provider/book");
ContentValues values = new ContentValues();
values.put("name", "Android开发艺术");
values.put("author", "任玉刚");
values.put("page_size", "500");
values.put("press", "机械工业出版社");
values.put("price", "67.8");
Uri uriReturn = getContentResolver().insert(uri, values);
Log.e(TAG, "onInsertClick: " + uriReturn);

// 查询所有书
Uri uri = Uri.parse("content://com.lucky.androidlearn.provider/book");
Cursor cursor = getContentResolver().query(uri, new String[]{"name", "press", "page_size", "price"}, null, null, null);
while (cursor != null && cursor.moveToNext()) {
    String name = cursor.getString(0);
    String press = cursor.getString(1);
    String pageSize = cursor.getString(2);
    String price = cursor.getString(3);
    Log.e(TAG, "onQueryClick: name " + name + " press " + press + " pageSize " + pageSize + " price " + price);
}
if (cursor != null) {
   cursor.close();
}

// 修改书
Uri uri = Uri.parse("content://com.lucky.androidlearn.provider/book");
ContentValues values = new ContentValues();
values.put("name", "Android开发艺术");
values.put("author", "任玉刚");
values.put("page_size", "478");
values.put("press", "机械工业出版社");
values.put("price", "78.9");
int updatedID = getContentResolver().update(uri, values, null, null);
Log.e(TAG, "onUpdateClick: " + updatedID);

// 删除书
Uri uri = Uri.parse("content://com.lucky.androidlearn.provider/book");
String where = "name=?";
String[] selectionArgs = new String[]{"Android开发艺术"};
//int deletedRows = getContentResolver().delete(uri, null, null);
// 删除指定条件的书籍
int deletedRows = getContentResolver().delete(uri, where, selectionArgs);
Log.e(TAG, "onDeleteClick: " + deletedRows);

```









