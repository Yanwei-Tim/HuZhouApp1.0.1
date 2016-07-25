package com.geekband.huzhouapp.utils;

/**
 * Created by Administrator on 2016/5/14
 */
public class Constants {
    //浏览器代理
    public final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36";
    //本地数据库名称
    public final static String NEWS_TABLE = "newsTable.db";
    //是否自动登录标识，contentId
    public final static String AUTO_LOGIN = "autoLogin";
    //用户真实姓名
    public final static String USER_REAL_NAME = "userRealName";
    //裁剪的头像名称
    public final static String AVATAR_IMAGE = "avatarImage";
    //服务器新闻传递标识
    public final static String LOCAL_NEWS_CONTENT = "localNewsContent";
    //用户课程表内容传递标识
    public final static String COURSE_CONTENT = "courseContent";
    //裁剪头像的存储路径
    public static final String AVATAR_URL = "avatarUrl";
    //图片类型
    public static final String IMAGE_TYPE = "image/*";
    public static final String IMAGE_CONTENT_ID = "imageContentId";
    //新闻是否滚动
    public static final String ROLLING = "1";
    public static final String UNROLLING = "0";
    public static final String SERVICE = "http://192.168.0.14:9080";//lmc本地测试用服务器;;
    public static final String BIRTHDAY_GIFT = "birthday_gift";

    // 请求返回成功码
    public static String REQUEST_RESULT_SUCCESS = "SUCCESS";
    //通知公告父类id以及序号
    public static final String INFORMATION_PARENT_ID = "A0100020166071447205284156";
    public static final String INFORMATION_SORT = "3";
    public static final String INFORMATION_CONTENT = "information_content";

    //广播
    public static final String GRADE_BROADCAST = "android.intent.action.GRADE_BROADCAST";
    public static final String BIRTHDAY_BROADCAST = "android.intent.action.BIRTHDAY_BROADCAST";
    //记录是否启通知
    public static final String IS_RECORD_GRADE ="isRecordGrade";
    public static final String IS_RECORD_BIRTHDAY ="isRecordBirthday";

}
