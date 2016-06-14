package com.geekband.huzhouapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/13
 */
public class FileUtils {

    public static String getCurrentTimeStr(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
}
