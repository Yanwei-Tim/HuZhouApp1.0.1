package com.chat.receiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;

import com.geekband.huzhouapp.activity.MainActivity;

public class MyReceiver extends BroadcastReceiver
{
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void onReceive(final Context context, Intent intent)
	{
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(context);
		builder.setSmallIcon(android.R.drawable.stat_notify_chat);
		//builder.setLargeIcon(null);
		builder.setAutoCancel(true);
		//builder.setContent(null); //设置自定义视图
		//builder.setOngoing(true);
		builder.setContentTitle("学分状况");
		builder.setContentText("未达标");
		builder.setTicker("有新通知");
		builder.setWhen(System.currentTimeMillis());
		builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));
		builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
		
		if(VERSION.SDK_INT<16)
		{
			notificationManager.notify(1, builder.getNotification());
		}
		else
		{
			builder.setPriority(Notification.PRIORITY_DEFAULT);
			notificationManager.notify(1, builder.build());
		}

	}
}
