package com.lucky.androidlearn.widget.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * PendingIntent的位标识符：
 * FLAG_ONE_SHOT   表示返回的PendingIntent仅能执行一次，执行完后自动取消
 * FLAG_NO_CREATE  表示如果描述的PendingIntent不存在，并不创建相应的PendingIntent，而是返回NULL
 * FLAG_CANCEL_CURRENT 表示相应的PendingIntent已经存在，则取消前者，然后创建新的PendingIntent，这个有利于数据保持为最新的，可以用于即时通信的通信场景
 * FLAG_UPDATE_CURRENT 表示更新的PendingIntent
 */
public class NotificationActivity extends AppCompatActivity {

    private static final int NORMAL_NOTIFICATION = 0;
    private static final int BIGTEXT_NOTIFICATION = 1;
    private static final int INBOX_NOTIFICATION = 2;
    private static final int BIGPICTURE_NOTIFICATION = 3;
    private static final int HANGUP_NOTIFICAIION = 4;
    private static final int CUSTOM_LAYOUT_NOTIFICATION = 5;
    private static final int MEDIA_NOTIFICATION = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_push_notification)
    public void pushNotification() {
        long when = System.currentTimeMillis() + 5 * 60 * 1000;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(NotificationActivity.this, TargetActivity.class);
        PendingIntent pdi = PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        Notification notification = new Notification.Builder(this)
                .setTicker("Ticker Text ...")
                .setContentTitle("Content Title")
                .setContentText("Content Text")
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(largeIcon)
                .setWhen(when) // setWhen 表示这个通知代表的事件将要发生的时间
                .setShowWhen(true)
                .setProgress(100, 20, false)
                .setContentIntent(pdi)
                .build();
        manager.notify(NORMAL_NOTIFICATION, notification);
    }

    @OnClick(R.id.btn_push_big_text)
    public void pushBigTextNotification() {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("BigTextStyle");
        builder.setContentText("BigTextStyle演示示例");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        androidx.core.app.NotificationCompat.BigTextStyle style = new androidx.core.app.NotificationCompat.BigTextStyle();
        style.bigText("这里是点击通知后要显示的正文，可以换行可以显示很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长");
        style.setBigContentTitle("点击后的标题");
        //SummaryText没什么用 可以不设置 style.setSummaryText("末尾只一行的文字内容");
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(this, TargetActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, 0);
        builder.setContentIntent(pIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        managerCompat.notify(BIGTEXT_NOTIFICATION, notification);
    }

    //InBoxStyle
    @OnClick(R.id.btn_push_inboxstyle_text)
    public void pushInBoxStyleNotification() {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("InboxStyle");
        builder.setContentText("InboxStyle演示示例");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        androidx.core.app.NotificationCompat.InboxStyle style = new androidx.core.app.NotificationCompat.InboxStyle();
        style.setBigContentTitle("BigContentTitle")
                .addLine("第一行，第一行，第一行，第一行，第一行，第一行，第一行")
                .addLine("第二行")
                .addLine("第三行")
                .addLine("第四行")
                .addLine("第五行")
                .setSummaryText("SummaryText");
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(this, TargetActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, 0);
        builder.setContentIntent(pIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        managerCompat.notify(INBOX_NOTIFICATION, notification);
    }

    // 大图通知模式
    @OnClick(R.id.btn_push_big_picture)
    public void pushBigPictureStyleNotification() {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("BigPictureStyle");
        builder.setContentText("BigPicture演示示例");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        androidx.core.app.NotificationCompat.BigPictureStyle style = new androidx.core.app.NotificationCompat.BigPictureStyle();
        style.setBigContentTitle("BigContentTitle");
        style.setSummaryText("SummaryText");
        style.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(this, TargetActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, 0);
        //设置点击大图后跳转
        builder.setContentIntent(pIntent);
        Notification notification = builder.build();
        managerCompat.notify(BIGPICTURE_NOTIFICATION, notification);
    }

    //悬挂模式
    //类似于手机QQ消息的通知，不显示在通知栏而是以横幅的模式显示在其他应用上方
    //  1. 此种效果只在5.0以上系统中有效
    //  2. manifest中需要添加
    //  3. 可能还需要在设置开启横幅通知权限（在设置通知管理中）
    //  4. 在部分改版rom上可能会直接弹出应用而不是显示横幅
    @OnClick(R.id.btn_push_hangup)
    public void hangUplNotification() {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("横幅通知");
        builder.setContentText("请在设置通知管理中开启消息横幅提醒权限");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        Intent intent = new Intent(this, TargetActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, 0);
        builder.setContentIntent(pIntent);
        //这句是重点
        builder.setFullScreenIntent(pIntent, true);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        managerCompat.notify(HANGUP_NOTIFICAIION, notification);
    }


    //MediaStyle
    //1. 使用类v7包下的NotificationCompat.MediaStyle
    //2. addAction方法并普通样式也可以用，使用后普通通知可以点击展开，展开部分会显示一排添加的图标，并且可以给每个图标设置不同的点击事件
    //3. 最多可以添加5哥action 并排显示在点击展开的部分
    //4. setShowActionsInCompactView的参数是添加的action在所有action组成的数组中的下标，从0开始
    //5. setShowActionsInCompactView设置的action会显示在点击前的通知的右侧，低版本上也可以显示
    //6. setShowCancelButton(true)会在通知的右上部分显示一个删除图标 5.0以下有效
    @OnClick(R.id.btn_push_media)
    public void mediaStyle() {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"media");
        builder.setContentTitle("MediaStyle");
        builder.setContentText("Song Title");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Intent intent = new Intent(this, TargetActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, 0);
        builder.setContentIntent(pIntent);
        //第一个参数是图标资源id 第二个是图标显示的名称，第三个图标点击要启动的PendingIntent
        builder.addAction(R.drawable.previous, "", null);
        builder.addAction(R.drawable.stop, "", null);
        builder.addAction(R.drawable.play, "", pIntent);
        builder.addAction(R.drawable.next, "", null);
        Notification.MediaStyle style = new Notification.MediaStyle();
//        style.setMediaSession(new MediaSession.Token(this, "MediaSession",
//                new ComponentName(TargetActivity.class.getName(), Intent.ACTION_MEDIA_BUTTON), null).getSessionToken());
//        //CancelButton在5.0以下的机器有效
//        style.setCancelButtonIntent(pIntent);
//        style.setShowCancelButton(true);
//        //设置要现实在通知右方的图标 最多三个
//        style.setShowActionsInCompactView(2, 3);
//        builder.setStyle(style);
        builder.setShowWhen(false);
        Notification notification = builder.build();
        managerCompat.notify(MEDIA_NOTIFICATION, notification);
    }

    // 自定义通知栏布局
    //1. 不同控件 PendingIntent.getXXX的requestCode不能相同
    //2. RemoteViews的具体用法请自行百度 这里就不展开说明了
    //3. 自定义布局的高需要是64dp 没有为什么 官方给的
    //4. 需要更改通知栏布局的时候 其实就是以同一个NotifyId发个新的通知 替换掉老的
    //5. LargeIcon可以不设置，但是smallIcon和title需要设置，不然通知不能显示
    //6. LargeIcon如果设置了并且自定义布局内相同位置还有一个icon的画在低版本系统上可能会都显示，高版本不会显示LargeIcon
    @OnClick(R.id.btn_push_custom_notification)
    public void pushCustomNotification(View view) {
        long when = System.currentTimeMillis() + 5 * 60 * 1000;
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        Intent intent = new Intent(this, TargetActivity.class);
        PendingIntent pdi = PendingIntent.getActivity(this, 0, intent, 0);
        RemoteViews remoteviews = new RemoteViews(getPackageName(), R.layout.remoteview);
        remoteviews.setTextViewText(R.id.tv_remoteview_1, "RemoteView1Text");
        remoteviews.setTextViewText(R.id.tv_remoteview_2, "RemoteView2Text");
        remoteviews.setOnClickPendingIntent(R.id.tv_remoteview_1, pdi);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setLargeIcon(largeIcon);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(10);
        builder.setContentIntent(pdi);
        builder.setContent(remoteviews);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT; // 表明某件事情正在进行
        notification.flags |= Notification.FLAG_NO_CLEAR; // 在点击清除的时候 不清楚该条通知
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        managerCompat.notify(CUSTOM_LAYOUT_NOTIFICATION, notification);
    }

    @OnClick(R.id.btn_custom_sound_notification)
    public void pushCustomSoundNotification(View view) {
        long when = System.currentTimeMillis() + 5 * 60 * 1000;
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        Intent intent = new Intent(this, TargetActivity.class);
        PendingIntent pdi = PendingIntent.getActivity(this, 0, intent, 0);
        RemoteViews remoteviews = new RemoteViews(getPackageName(), R.layout.remoteview);
        remoteviews.setTextViewText(R.id.tv_remoteview_1, "RemoteView1Text");
        remoteviews.setTextViewText(R.id.tv_remoteview_2, "RemoteView2Text");
        remoteviews.setOnClickPendingIntent(R.id.tv_remoteview_1, pdi);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setLargeIcon(largeIcon);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(10);
        builder.setContentIntent(pdi);
        builder.setContent(remoteviews);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT; // 表明某件事情正在进行
        notification.flags |= Notification.FLAG_NO_CLEAR; // 在点击清除的时候 不清楚该条通知
        //notification.defaults |= Notification.DEFAULT_SOUND;
        //notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        //获取设置通知的声音
        Uri sound0=Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notificationsound);
        Uri sound1=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/notificationsound");
        Uri sound2=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/"+R.raw.notificationsound);
        //从铃声管理器
        Uri sound3= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //从文件
        //Uri sound4=Uri.fromFile(new File("/sdcard/sound.mp3"))
        //Uri sound5=Uri.parse(new File("/sdcard/sound.mp3").toString()));

        //设置通知的声音
        notification.sound = sound3;
        managerCompat.notify(CUSTOM_LAYOUT_NOTIFICATION, notification);
    }


    @OnClick(R.id.btn_clear_notifications)
    public void clearAllNotifications(View view) {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.cancelAll();
    }

    @OnClick(R.id.btn_progress_notification)
    public void pushProgressNotifications(View view) {
        managerCompat = NotificationManagerCompat.from(this);
        Intent intent = new Intent(NotificationActivity.this, TargetActivity.class);
        PendingIntent pdi = PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        builder = new NotificationCompat.Builder(this)
                .setTicker("Ticker Text ...")
                .setContentTitle("Content Title")
                .setContentText("Content Text")
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(largeIcon)
                .setWhen(System.currentTimeMillis()) // setWhen 表示这个通知代表的事件将要发生的时间
                .setShowWhen(true)
                .setProgress(100, 0, false)
                .setOngoing(true)
                .setAutoCancel(false)
                .setContentIntent(pdi);
        managerCompat.notify(NORMAL_NOTIFICATION, builder.build());
        new Thread(new UpdateProgressTask(mUpdateProgressHandler)).start();
    }

    NotificationManagerCompat managerCompat;
    NotificationCompat.Builder builder;
    private UpdateProgressHandler mUpdateProgressHandler = new UpdateProgressHandler();

    class UpdateProgressHandler extends Handler {
        private static final String TAG = "UpdateProgressHandler";

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.e(TAG, "handleMessage: " + msg.arg1);
                    builder.setProgress(100, msg.arg1, false);
                    managerCompat.notify(NORMAL_NOTIFICATION, builder.build());
                    break;
                case 1:
                    managerCompat.cancel(NORMAL_NOTIFICATION);
                    break;
            }
        }
    }

    private class UpdateProgressTask implements Runnable {
        private Handler mUpdateProgressHandler;

        public UpdateProgressTask(Handler handler) {
            this.mUpdateProgressHandler = handler;
        }

        @Override
        public void run() {
            try {
                for (int index = 0; index <= 10000; index++) {
                    if (index % 100 == 0) {
                        Message message = Message.obtain();
                        message.what = 0;
                        message.arg1 = index / 100;
                        mUpdateProgressHandler.sendMessage(message);
                        Thread.sleep(1000);
                    }
                }
                Message message = Message.obtain();
                message.what = 1;
                mUpdateProgressHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
