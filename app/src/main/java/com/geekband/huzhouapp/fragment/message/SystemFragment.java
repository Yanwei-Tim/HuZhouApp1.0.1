package com.geekband.huzhouapp.fragment.message;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.activity.MainActivity;

/**
 * Created by Administrator on 2016/5/12
 */
public class SystemFragment extends Fragment {

    MainActivity mMainActivity;

    public static SystemFragment newInstance() {
        return new SystemFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system, null);

        return view;
    }

//    /**
//     * 初始化通知栏
//     */
//    private void initNotify() {
//        mBuilder = new NotificationCompat.Builder(getActivity());
//    }
//
//    /**
//     * 显示通知栏
//     */
//    public void showNotify() {
//        mBuilder.setSmallIcon(R.drawable.app_icon_message) //设置通知  消息  图标
//                .setContentTitle("系统信息")
//                .setContentText("您目前学分未达标(点击可查看具体信息)")
//                .setWhen(System.currentTimeMillis())
//                .setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_VIBRATE)
////				.setNumber(number)//显示数量
//                .setTicker("学分通知来啦");//通知首次出现在通知栏，带上升动画效果的
//        mMainActivity.mNotificationManager.notify(notifyId, mBuilder.build());
//    }


//    /**
//     * 显示通知栏点击跳转到指定Activity
//     */
//    public void showIntentActivityNotify() {
//        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
////		notification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
//        mBuilder.setSmallIcon(R.drawable.app_icon_message) //设置通知  消息  图标
//                .setContentTitle("系统信息")
//                .setContentText("您目前学分未达标(点击可查看具体信息)")
//                .setWhen(System.currentTimeMillis())
//                .setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_VIBRATE)
//                .setTicker("学分通知来啦");
//        //点击的意图ACTION是跳转到Intent
//        Intent resultIntent = new Intent(getActivity(), GradeActivity.class);
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(pendingIntent);
//        mMainActivity.mNotificationManager.notify(notifyId, mBuilder.build());
//    }

}
