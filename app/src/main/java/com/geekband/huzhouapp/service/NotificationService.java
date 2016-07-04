package com.geekband.huzhouapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.utils.DataUtils;
import com.geekband.huzhouapp.utils.Constants;
import com.geekband.huzhouapp.vo.BirthdayInfo;
import com.geekband.huzhouapp.vo.GradeInfo;
import com.lidroid.xutils.exception.DbException;

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
    private Intent mIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mIntent = new Intent("android.intent.action.NOTIFICATION_BROADCAST");
        startTimer();

    }

    private void startTimer() {
        String time = "10:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd "+time);
        try {
            String newTime= sdf.format(new Date());
//            Log.i("日期转格式", newTime);
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(newTime);
//            Log.i("格式转日期",startTime.toString());
            Timer timer = new Timer();
            timer.schedule(gradeTimer, startTime, 24 * 60*60 * 1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void sendNotification() {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String contentId = MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, null);
                    DataUtils.saveGrade(contentId);
                    try {
                        GradeInfo gradeInfo = MyApplication.sDbUtils.findFirst(GradeInfo.class);
                        if (gradeInfo != null) {
                            String needGrade = gradeInfo.getNeedGrade();
                            String alreadyGrade = gradeInfo.getAlreadyGrade();
                            if ((Integer.parseInt(alreadyGrade) < Integer.parseInt(needGrade))) {
                                mIntent.setAction("gradeMessage");
                                mIntent.putExtra("gradeMessage", "您目前学分未达标(点击可查看具体信息)");
                                sendBroadcast(mIntent);
                            }
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }


                }
            }).start();

    }

    public void birthdayNotification(){
        new Thread(){
            @Override
            public void run() {
                ArrayList<BirthdayInfo> birthdayInfos = DataUtils.getBirthdayInfo(10,1);
                if (birthdayInfos!=null&&birthdayInfos.size()!=0){
                    mIntent.setAction("birthdayMessage");
                    mIntent.putExtra("birthdayMessage", "有朋友今天生日，赶快送上祝福吧！(点击可查看名单)");
                    sendBroadcast(mIntent);
                }
            }
        }.start();
    }

    private Handler notificationHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case GRADE_TIMER:
                    sendNotification();
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
}
