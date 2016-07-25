package com.geekband.huzhouapp.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.utils.DataUtils;
import com.geekband.huzhouapp.vo.BirthdayInfo;
import com.geekband.huzhouapp.vo.GradeInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/6/3
 */
public class NotificationService extends Service {

    private static final int GRADE_TIMER = 1;
    private static final int BIRTHDAY_TIMER = 2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startTimer();

    }


    private void startTimer() {
        String time = "10:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + time);
        try {
            String newTime = sdf.format(new Date());
//            Log.i("日期转格式", newTime);
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(newTime);
//            Log.i("格式转日期",startTime.toString());
            Timer timer = new Timer();
            //判断今天是否启动过生日通知
            String currentDateStr = new SimpleDateFormat("yyMMdd").format(new Date());
            if (!currentDateStr.equals(MyApplication.sSharedPreferences.getString(Constants.IS_RECORD_GRADE, null))) {

                timer.schedule(gradeTimer, startTime, 24 * 60 * 60 * 1000);

            }
            timer.schedule(birthdayTimer, startTime, 24 * 60 * 60 * 1000);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void sendGradeNotification() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String contentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, null);
                GradeInfo gradeInfo = DataUtils.getGrade(contentId);
                if (gradeInfo != null) {
                    String needGrade = gradeInfo.getNeedGrade();
                    String alreadyGrade = gradeInfo.getAlreadyGrade();
                    if ((Integer.parseInt(alreadyGrade) < Integer.parseInt(needGrade))) {
                        Intent intent = new Intent(Constants.GRADE_BROADCAST);
                        intent.putExtra("gradeMessage", "您目前学分未达标(点击可查看具体信息)");
                        sendBroadcast(intent);
                        Log.i(NotificationService.class.getSimpleName() + "关注：", "学分通知已发送");
                    }
                }

            }
        }).start();

    }

    public void sendBirthdayNotification() {
        new Thread() {
            @Override
            public void run() {
                ArrayList<BirthdayInfo> users = DataUtils.getBirthdayInfo();
                for (BirthdayInfo birthdayInfo : users) {
                    if (birthdayInfo.getDate() != null && DataUtils.getDays(birthdayInfo.getDate()).equals("0")) {
                        Intent intent = new Intent("android.intent.action.BIRTHDAY_BROADCAST");
                        intent.putExtra("birthdayMessage", "有朋友今天生日，赶快送上祝福吧！(点击可查看名单)");
                        sendBroadcast(intent);
                    }
                }
            }
        }.start();

    }

    private Handler notificationHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case GRADE_TIMER:
                    sendGradeNotification();
                    recordDate(Constants.IS_RECORD_GRADE);
                    break;
                case BIRTHDAY_TIMER:
                    sendBirthdayNotification();
                    break;

            }
            return false;
        }
    });

    private TimerTask gradeTimer = new TimerTask() {
        @Override
        public void run() {
            Message message = Message.obtain();
            message.what = GRADE_TIMER;
            notificationHandler.sendMessage(message);
        }
    };

    private TimerTask birthdayTimer = new TimerTask() {
        @Override
        public void run() {
            Message message = Message.obtain();
            message.what = BIRTHDAY_TIMER;
            notificationHandler.sendMessage(message);
        }
    };


    public void recordDate(String flag) {
        //当前时间
        Date currentDate = new Date();
        String currentDateStr = new SimpleDateFormat("yyMMdd").format(currentDate);
        SharedPreferences.Editor editor = MyApplication.sSharedPreferences.edit();
        editor.putString(flag, currentDateStr);
        editor.apply();
    }

}
