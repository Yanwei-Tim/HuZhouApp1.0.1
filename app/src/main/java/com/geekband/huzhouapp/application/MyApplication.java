package com.geekband.huzhouapp.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.geekband.huzhouapp.utils.Constants;
import com.lidroid.xutils.DbUtils;

/**
 * Created by Administrator on 2016/5/15
 */
public class MyApplication extends Application {

    public static DbUtils sDbUtils;
    public static SharedPreferences sSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sDbUtils = DbUtils.create(getApplicationContext(), Constants.NEWS_TABLE);
        sSharedPreferences = getSharedPreferences(Constants.AUTO_LOGIN,MODE_PRIVATE);


    }
}
