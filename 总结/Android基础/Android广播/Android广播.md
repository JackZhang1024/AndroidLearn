- [Android广播](#android广播)
  - [Android两种广播的区别](#android两种广播的区别)
  - [LocalBroadCast本地广播的原理](#localbroadcast本地广播的原理)
  - [BroadCast全局广播的原理](#broadcast全局广播的原理)
## Android广播
### Android两种广播的区别
LocalBroadCast顾名思义就是本地广播，区别于BroadCast全局广播，它只是在应用内进行广播的发送和接收，所以不必担心私有的数据泄露出去，同时外边的广播也是发送不进来的。BroadCast是可以在应用内部，也可以在应用间进行广播消息传播的，所以存在隐私数据的泄露可能。
### LocalBroadCast本地广播的原理
LocalBroadCast利用的是Handler机制进行消息发送和接收处理的。
```java
//LocalBroadCastManager.java
public void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter) {
    synchronized (mReceivers) {
        ReceiverRecord entry = new ReceiverRecord(filter, receiver);
        ArrayList<ReceiverRecord> filters = mReceivers.get(receiver);
        if (filters == null) {
            filters = new ArrayList<>(1);
            mReceivers.put(receiver, filters);
        }
        filters.add(entry);
        for (int i=0; i<filter.countActions(); i++) {
             String action = filter.getAction(i);
             ArrayList<ReceiverRecord> entries = mActions.get(action);
             if (entries == null) {
                 entries = new ArrayList<ReceiverRecord>(1);
                 mActions.put(action, entries);
             }
             entries.add(entry);
        }
    }
}    

public boolean sendBroadcast(@NonNull Intent intent) {
    synchronized (mReceivers) {
        final String action = intent.getAction();
        final String type = intent.resolveTypeIfNeeded(mAppContext.getContentResolver());
        final Uri data = intent.getData();
        final String scheme = intent.getScheme();
        final Set<String> categories = intent.getCategories()
        final boolean debug = DEBUG ||
                ((intent.getFlags() & Intent.FLAG_DEBUG_LOG_RESOLUTION) != 0);

        ArrayList<ReceiverRecord> entries = mActions.get(intent.getAction());
        if (entries != null) {
            if (debug) Log.v(TAG, "Action list: " + entries)
            ArrayList<ReceiverRecord> receivers = null;
            for (int i=0; i<entries.size(); i++) {
                ReceiverRecord receiver = entries.get(i);
                if (debug) Log.v(TAG, "Matching against filter " + receiver.filter);

                if (receiver.broadcasting) {
                    if (debug) {
                         Log.v(TAG, "  Filter's target already added");
                    }
                    continue;
                
                int match = receiver.filter.match(action, type, scheme, data,
                        categories, "LocalBroadcastManager");
                if (match >= 0) {
                    if (debug) Log.v(TAG, "  Filter matched!  match=0x" +
                            Integer.toHexString(match));
                    if (receivers == null) {
                        receivers = new ArrayList<ReceiverRecord>();
                    }
                    receivers.add(receiver);
                    receiver.broadcasting = true;
                } else {
                  ....
                }
            }
            //筛选出所有符合条件的Receiver
            if (receivers != null) {
                for (int i=0; i<receivers.size(); i++) {
                    receivers.get(i).broadcasting = false;
                }
                mPendingBroadcasts.add(new BroadcastRecord(intent, receivers));
                if (!mHandler.hasMessages(MSG_EXEC_PENDING_BROADCASTS)) {
                    // 发送消息到Handler 然后开始处理消息
                    mHandler.sendEmptyMessage(MSG_EXEC_PENDING_BROADCASTS);
                }
                return true;
            }
        }
    }
    return false;
}

private LocalBroadcastManager(Context context) {
    mAppContext = context;
    mHandler = new Handler(context.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_EXEC_PENDING_BROADCASTS:
                    //处理消息
                    executePendingBroadcasts();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
}

void executePendingBroadcasts() {
    while (true) {
        final BroadcastRecord[] brs;
        synchronized (mReceivers) {
            final int N = mPendingBroadcasts.size();
            if (N <= 0) {
                return;
            }
            //创建一个临时数组，然后清空pendingBroadcasts列表
            brs = new BroadcastRecord[N];
            mPendingBroadcasts.toArray(brs);
            mPendingBroadcasts.clear();
        }
        //遍历出所有广播接收者
        for (int i=0; i<brs.length; i++) {
            final BroadcastRecord br = brs[i];
            final int nbr = br.receivers.size();
            for (int j=0; j<nbr; j++) {
                final ReceiverRecord rec = br.receivers.get(j);
                if (!rec.dead) {
                    rec.receiver.onReceive(mAppContext, br.intent);
                }
            }
        }
    }
}

```

### BroadCast全局广播的原理 
BroadCast是利用Binder进行进程间消息发送和接收处理的，后面在高级部分会分析四大组件的工作流程。
