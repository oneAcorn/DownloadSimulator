package com.acorn.downloadsimulator;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Created by acorn on 2020/4/10.
 */
public class DownloadService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForeground(102, createNotification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopSelf();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    private Notification createNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelID = "DownloadService";

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelID, "下载漫画服务", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true); //设置提示灯
            channel.setLightColor(Color.GREEN); //提示灯颜色
            channel.setShowBadge(true); //显示logo
            channel.setDescription("下载漫画呢"); //设置描述
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见
            manager.createNotificationChannel(channel);

            Notification.Builder builder = new Notification.Builder(getApplicationContext());
            builder
                    .setChannelId(channelID)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)) //设置下拉菜单中的图标
                    .setContentTitle("下载图片服务启动中") //设置下拉菜单的标题
                    .setSmallIcon(R.mipmap.ic_launcher) //设置状态栏的小图标
                    .setContentText("要显示的内容") //设置上下文内容
                    .setWhen(System.currentTimeMillis()); //设置通知发生时间
            return builder.build();
        } else
            return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
