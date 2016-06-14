package com.geekband.huzhouapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.fragment.news.GradeActivity;

/**
 * Created by Administrator on 2016/6/2
 */
public class NotificationBroadCast extends BroadcastReceiver {

    Context mContext;
    NotificationCompat.Builder mBuilder;
    public NotificationManager mNotificationManager;

    /**
     * Notification的ID
     */
    int notifyId = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        initService();
        initNotify();
        String msg = intent.getStringExtra("msg");
        showIntentActivityNotify(msg);
    }

    /**
     * 初始化要用到的系统服务
     */
    private void initService() {
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        mBuilder = new NotificationCompat.Builder(mContext);
    }

    /**
     * 清除当前创建的通知栏
     */
    public void clearNotify(int notifyId) {
        mNotificationManager.cancel(notifyId);//删除一个特定的通知ID对应的通知
//		mNotification.cancel(getResources().getString(R.string.app_name));
    }

    /**
     * 清除所有通知栏
     */
    public void clearAllNotify() {
        mNotificationManager.cancelAll();// 删除你发的所有通知
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, new Intent(), flags);
        return pendingIntent;
    }

        /**
     * 显示通知栏点击跳转到指定Activity
     */
    public void showIntentActivityNotify(String msg) {
        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
//		notification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
        mBuilder.setSmallIcon(R.drawable.app_icon_message) //设置通知  消息  图标
                .setContentTitle("系统信息")
                .setContentText(msg)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setTicker("学分通知来啦");
        //点击的意图ACTION是跳转到Intent
        Intent resultIntent = new Intent(mContext, GradeActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }


}
