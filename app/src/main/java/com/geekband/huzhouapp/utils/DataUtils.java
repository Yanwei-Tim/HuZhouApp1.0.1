package com.geekband.huzhouapp.utils;

import android.os.AsyncTask;

import com.database.dto.DataOperation;
import com.database.pojo.AlbumTable;
import com.database.pojo.CategoriesTable;
import com.database.pojo.ContentTable;
import com.database.pojo.CourseTable;
import com.database.pojo.CommonTable;
import com.database.pojo.DepartmentsTable;
import com.database.pojo.StudyInfoTable;
import com.database.pojo.UserInfoTable;
import com.database.pojo.UserTable;
import com.geekband.huzhouapp.activity.SplashActivity;
import com.geekband.huzhouapp.application.MyApplication;
import com.geekband.huzhouapp.vo.AlbumInfo;
import com.geekband.huzhouapp.vo.BirthdayInfo;
import com.geekband.huzhouapp.vo.CourseInfo;
import com.geekband.huzhouapp.vo.DynamicNews;
import com.geekband.huzhouapp.vo.GradeInfo;
import com.geekband.huzhouapp.vo.NetNewsInfo;
import com.geekband.huzhouapp.vo.UserBaseInfo;
import com.lidroid.xutils.exception.DbException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/25
 */
public class DataUtils {
    /**
     * @return 分页获取本地新闻信息
     */
    public static ArrayList<DynamicNews> getNewsList(String isRolling, int pageSize, int currentPage) {
        ArrayList<DynamicNews> localNewsList = new ArrayList<>();
        //查询分类表
        //noinspection unchecked
        ArrayList<CategoriesTable> categoriesTables = (ArrayList<CategoriesTable>) DataOperation.
                queryTable(CategoriesTable.TABLE_NAME, CategoriesTable.FIELD_PARENTID, "A0100020166071445164939060");
        if (categoriesTables != null && categoriesTables.size() != 0) {
            String newsId = categoriesTables.get(0).getContentId();
            //在通用信息表里面匹配新闻
            Map<String, String> newsCategory = new HashMap<>();
            newsCategory.put(CommonTable.FIELD_CATEGORYID, newsId);
            newsCategory.put(CommonTable.FIELD_ISROLLING, isRolling);
            //noinspection unchecked
            ArrayList<CommonTable> commonTables = (ArrayList<CommonTable>) DataOperation.queryTable(CommonTable.TABLE_NAME, currentPage, pageSize, newsCategory);
            if (commonTables != null) {
                for (int i = 0; i < commonTables.size(); i++) {
                    DynamicNews localNews = new DynamicNews();
                    String title = commonTables.get(i).getField(CommonTable.FIELD_TITLE);
                    String writer = commonTables.get(i).getField(CommonTable.FIELD_WRITERID);
                    //noinspection unchecked
                    ArrayList<UserTable> writerTables = (ArrayList<UserTable>) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, writer);
                    String writerId = null;
                    if (writerTables != null && writerTables.size() != 0) {
                        writerId = writerTables.get(0).getField(UserTable.FIELD_REALNAME);
                    }
                    String auditor = commonTables.get(i).getField(CommonTable.FIELD_AUDITOR);
                    String auditorId = null;
                    //noinspection unchecked
                    ArrayList<DepartmentsTable> departmentsTables = (ArrayList<DepartmentsTable>) DataOperation.queryTable(DepartmentsTable.TABLE_NAME, DepartmentsTable.CONTENTID, auditor);
                    if (departmentsTables != null && departmentsTables.size() != 0) {
                        auditorId = departmentsTables.get(0).getField(DepartmentsTable.FIELD_DEPARTMENTNAME);
                    }

                    String date = commonTables.get(i).getField(CommonTable.FIELD_DATETIME);
                    String picUrl = null;
                    if (isRolling.equals("0")) {
                        ArrayList<String> picUrls = (ArrayList<String>) commonTables.get(i).getAccessaryFileUrlList();
                        if (picUrls.size() != 0) {
                            picUrl = picUrls.get(0);
                        }
                    } else if (isRolling.equals("1")) {
                        picUrl = "http://" + com.oa.util.Constants.CONNIP + commonTables.get(i).getField(CommonTable.FIELD_PICURL);
                    }
                    String contentID = commonTables.get(i).getContentId();

                    //根据contentId获取新闻内容
                    String content = null;
                    //noinspection unchecked
                    ArrayList<ContentTable> contentTables = (ArrayList<ContentTable>) DataOperation.queryTable(ContentTable.TABLE_NAME, ContentTable.FIELD_NEWSID, contentID);
//                System.out.println("测试新闻内容图片"+contentTables);
                    if (contentTables != null && contentTables.size() != 0) {
                        content = contentTables.get(0).getField(ContentTable.FIELD_SUBSTANCE);
                        //将获取的新闻信息放入本地LocalNews
                    }
                    localNews.setTitle(title);
                    localNews.setWriterId(writerId);
                    localNews.setAuditorId(auditorId);
                    localNews.setPicUrl(picUrl);
                    localNews.setDate(date);
                    localNews.setContent(content);
                    localNews.setId(i);
                    localNewsList.add(localNews);
                }
            }
        }

        return localNewsList;
    }

    /**
     * @param url 新闻地址
     * @return 新闻信息集合
     */
    public static ArrayList<NetNewsInfo> loadNetNews(String url) {
        ArrayList<NetNewsInfo> newsList = new ArrayList<>();
        String userAgent = Constants.USER_AGENT;
        try {
            Document document = Jsoup.connect(url).userAgent(userAgent).timeout(5000).get();
            Elements newsElements = document.select("div.at");
            for (int i = 1; i < newsElements.size(); i++) {
                Elements titleElements = newsElements.get(i).getElementsByTag("h3");
                String newsTitle = titleElements.text();
                Elements picElements = newsElements.get(i).getElementsByTag("img");
                String newsHTML = newsElements.get(i).attr("href");
                String newsPic = picElements.attr("src");
                //消除部分图片乱码
                newsPic = newsPic.substring(0, newsPic.lastIndexOf(".")) + ".jpg";
                NetNewsInfo newsInfo = new NetNewsInfo();
                newsInfo.setNewsTitle(newsTitle);
                newsInfo.setNewsPic(newsPic);
                newsInfo.setNewsHTML(newsHTML);
                newsInfo.setId(i);
                newsList.add(newsInfo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsList;
    }


    /**
     * 保存网络新闻到本地数据库
     */
    public static void saveNetNews() {
        new NetNewsTask().execute();
    }

    static class NetNewsTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            ArrayList<NetNewsInfo> netNewsInfoList = loadNetNews("http://news.sohu.com/photo/");
            if (netNewsInfoList != null) {
                try {
                    MyApplication.sDbUtils.deleteAll(NetNewsInfo.class);
                    MyApplication.sDbUtils.saveAll(netNewsInfoList);
                } catch (DbException e) {
                    e.printStackTrace();//无本地数据
                }
            }
            return null;
        }

    }

    /**
     * 保存基本用户信息
     */

    public static void saveUserBaseInfo(String contentId) {
        new BaseInfoTsk().execute(contentId);
    }

    static class BaseInfoTsk extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String contentId = params[0];
            UserBaseInfo userBaseInfo = new UserBaseInfo();

            //noinspection unchecked
            ArrayList<UserTable> userTables = (ArrayList<UserTable>) DataOperation.queryTable(UserTable.TABLE_NAME, UserTable.CONTENTID, contentId);
            if (userTables != null && userTables.size() != 0) {
                UserTable userTable = userTables.get(0);
                if (userTable != null) {
                    int id = 0;
                    String userName = userTable.getField(UserTable.FIELD_USERNAME);
                    String realName = userTable.getField(UserTable.FIELD_REALNAME);
                    String phoneNum = userTable.getField(UserTable.FIELD_TELEPHONE);
                    String emailAddress = userTable.getField(UserTable.FIELD_EMAIL);

                    UserInfoTable userInfoTable = (UserInfoTable) DataOperation.queryTable(UserInfoTable.TABLE_NAME, UserInfoTable.FIELD_USERID, contentId).get(0);
                    if (userInfoTable != null) {
                        String sex = userInfoTable.getField(UserInfoTable.FIELD_SEX);
                        String address = userInfoTable.getField(UserInfoTable.FIELD_ADDRESS);
                        String birthday = userInfoTable.getField(UserInfoTable.FIELD_BIRTHDAY);

                        userBaseInfo.setId(id);
                        userBaseInfo.setUserName(userName);
                        userBaseInfo.setContentId(contentId);
                        userBaseInfo.setRealName(realName);
                        userBaseInfo.setPhoneNum(phoneNum);
                        userBaseInfo.setEmailAddress(emailAddress);
                        userBaseInfo.setSex(sex);
                        userBaseInfo.setAddress(address);
                        userBaseInfo.setBirthday(birthday);

                        try {
                            MyApplication.sDbUtils.deleteAll(UserBaseInfo.class);
                            MyApplication.sDbUtils.save(userBaseInfo);

                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }
    }

    /**
     * 保存相册信息
     */
    public static void saveAlbum(String contentId) {
        new AlbumTask().execute(contentId);
    }

    static class AlbumTask extends AsyncTask<String, Integer, Integer> {
        ArrayList<AlbumTable> mAlbumTableList;
        //相册基本信息
        ArrayList<AlbumInfo> mAlbumInfoList;

        @Override
        protected Integer doInBackground(String... params) {
            //noinspection unchecked
            mAlbumTableList = (ArrayList<AlbumTable>) DataOperation.queryTable(AlbumTable.TABLE_NAME, AlbumTable.FIELD_USERID, params[0]);
            //相册信息列表
            mAlbumInfoList = new ArrayList<>();

            if (mAlbumTableList != null) {
                for (int i = 0; i < mAlbumTableList.size(); i++) {
                    //获取单个相册及其名称和所包含的相册数量
                    AlbumTable albumTable = mAlbumTableList.get(i);
                    String albumName = albumTable.getField(AlbumTable.FIELD_NAME);
                    String contentId = albumTable.getContentId();
                    List<String> phoUrlList = albumTable.getAccessaryFileUrlList();
                    int albumCount = phoUrlList.size();
                    AlbumInfo albumInfo = new AlbumInfo();
                    //相册封面默认第一张
                    if (phoUrlList.size() != 0) {//当当前相册没有图片时不予展示
                        albumInfo.setAlbumUrl(phoUrlList.get(0));
                        albumInfo.setAlbumName(albumName);
                        albumInfo.setAlbumCount(albumCount);
                        albumInfo.setContentId(contentId);
                        albumInfo.setId(i);
                        mAlbumInfoList.add(albumInfo);
                    }
                }

                try {
                    MyApplication.sDbUtils.deleteAll(AlbumInfo.class);
                    MyApplication.sDbUtils.saveAll(mAlbumInfoList);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                return 1;//有相册
            } else {
                return 2;//无相册
            }

        }

    }

    /**
     * 查询课程信息
     */
    public static void saveCourse(String contentId) {
        new CourseTask().execute(contentId);
    }

    static class CourseTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            ArrayList<CourseInfo> courseList = new ArrayList<>();
            //noinspection unchecked
            ArrayList<StudyInfoTable> StudyInfoTables = (ArrayList<StudyInfoTable>) DataOperation.queryTable(StudyInfoTable.TABLE_NAME, StudyInfoTable.FIELD_USERNO, params[0]);
            if (StudyInfoTables != null) {
                for (int i = 0; i < StudyInfoTables.size(); i++) {
                    String courseNo = StudyInfoTables.get(i).getField(StudyInfoTable.FIELD_COURSENO);
                    //noinspection unchecked
                    ArrayList<CourseTable> courseTables = (ArrayList<CourseTable>) DataOperation.queryTable(CourseTable.TABLE_NAME, CourseTable.CONTENTID, courseNo);
                    if (courseTables != null && courseTables.size() != 0) {
                        CourseTable courseTable = courseTables.get(0);
                        if (courseTable != null) {
                            CourseInfo courseInfo = new CourseInfo();
                            //课程名
                            String title = courseTable.getField(CourseTable.FIELD_COURSENAME);
                            //选修必修
                            String type = courseTable.getField(CourseTable.FIELD_COURSETYPE);
                            //课程简介
                            String intro = courseTable.getField(CourseTable.FIELD_COURSEINTRO);
                            //详细内容
                            String detailed = courseTable.getField(CourseTable.FIELD_DETAILED);
                            //积分
                            String point = courseTable.getField(CourseTable.FIELD_POINT);
                            //学习时长
                            String time = courseTable.getField(CourseTable.FIELD_NEEDTIME);

                            courseInfo.setId(i);
                            courseInfo.setTitle(title);
                            courseInfo.setType(type);
                            courseInfo.setPoint(point);
                            courseInfo.setIntro(intro);
                            courseInfo.setTime(time);
                            courseInfo.setDetailed(detailed);
                            courseList.add(courseInfo);
                        }
                    }
                }
                //保存到本地
                try {
                    MyApplication.sDbUtils.deleteAll(CourseInfo.class);
                    MyApplication.sDbUtils.saveAll(courseList);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    /**
     * 获取个人课程
     *
     * @return
     */
    public static ArrayList<CourseInfo> bindCourseInfo() {
        ArrayList<CourseInfo> courseList = new ArrayList<>();
        //noinspection unchecked
        ArrayList<StudyInfoTable> StudyInfoTables = (ArrayList<StudyInfoTable>) DataOperation
                .queryTable(StudyInfoTable.TABLE_NAME, StudyInfoTable.FIELD_USERNO
                        , MyApplication.sSharedPreferences.getString(Constants.AUTO_LOGIN, null));
        if (StudyInfoTables != null) {
            for (int i = 0; i < StudyInfoTables.size(); i++) {
                String courseNo = StudyInfoTables.get(i).getField(StudyInfoTable.FIELD_COURSENO);
                //noinspection unchecked
                ArrayList<CourseTable> courseTables = (ArrayList<CourseTable>) DataOperation.queryTable(CourseTable.TABLE_NAME, CourseTable.CONTENTID, courseNo);
                if (courseTables != null && courseTables.size() != 0) {
                    CourseTable courseTable = courseTables.get(0);
                    if (courseTable != null) {
                        CourseInfo courseInfo = new CourseInfo();
                        //课程名
                        String title = courseTable.getField(CourseTable.FIELD_COURSENAME);
                        //选修必修
                        String type = courseTable.getField(CourseTable.FIELD_COURSETYPE);
                        //课程简介
                        String intro = courseTable.getField(CourseTable.FIELD_COURSEINTRO);
                        //详细内容
                        String detailed = courseTable.getField(CourseTable.FIELD_DETAILED);
                        //积分
                        String point = courseTable.getField(CourseTable.FIELD_POINT);
                        //学习时长
                        String time = courseTable.getField(CourseTable.FIELD_NEEDTIME);

                        courseInfo.setId(i + 1);
                        courseInfo.setTitle(title);
                        courseInfo.setType(type);
                        courseInfo.setPoint(point);
                        courseInfo.setIntro(intro);
                        courseInfo.setTime(time);
                        courseInfo.setDetailed(detailed);
                        courseList.add(courseInfo);
                    }
                }
            }
        }
        return courseList;
    }

    /**
     * 获取发布的课程
     */

    public static ArrayList<CourseTable> getAllCourses() {
        //noinspection unchecked
        return (ArrayList<CourseTable>) DataOperation.queryTable(CourseTable.TABLE_NAME);
    }


    /**
     * 获取生日在当前日期内的所有用户真实姓名及其生日
     */

    public static ArrayList<BirthdayInfo> getBirthdayInfo(int pageSize, int currentPage) {
        ArrayList<BirthdayInfo> arrayList = new ArrayList<>();
        //noinspection unchecked
        ArrayList<UserTable> userTables = (ArrayList<UserTable>) DataOperation.queryTable(UserTable.TABLE_NAME, currentPage, pageSize, (Map<String, String>) null);
        if (userTables != null && userTables.size() != 0) {
            for (UserTable userTable : userTables) {
                String userId = userTable.getContentId();
                //noinspection unchecked
                ArrayList<UserInfoTable> userInfoTables = (ArrayList<UserInfoTable>) DataOperation.queryTable(UserInfoTable.TABLE_NAME, UserInfoTable.FIELD_USERID, userId);
                if (userInfoTables != null && userInfoTables.size() != 0) {
                    //真实姓名
                    String realName = userTable.getField(UserTable.FIELD_REALNAME);
                    //生日
                    String idCardNum = userInfoTables.get(0).getField(UserInfoTable.FIELD_IDCARDNO);
                    String birthdayStr;
                    if (idCardNum != null && idCardNum.length() == 18) {
                        birthdayStr = idCardNum.substring(11, 15);
                    } else {
                        birthdayStr = "";
                    }
                    //头像地址
                    ArrayList<String> avatarUrls = (ArrayList<String>) userTable.getAccessaryFileUrlList();
                    String avatarUrl = avatarUrls.get(0);

                    BirthdayInfo birthdayInfo = new BirthdayInfo();
                    birthdayInfo.setRealName(realName);
                    birthdayInfo.setDate(birthdayStr);
                    birthdayInfo.setAvatarImage(avatarUrl);

                    arrayList.add(birthdayInfo);
                }

            }
        }
        return arrayList;
    }

    /**
     * 保存成绩信息
     */
    public static void saveGrade(String contentId) {
        new GradeTask().execute(contentId);
    }

    static class GradeTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            int id = 0;
            //获取需修学分
            int needScore = DataOperation.queryUserNeedScore(params[0]);
            //获取已修学分
            int currentScore = DataOperation.queryUserCurrentScore(params[0]);
            //必修课程
            //选修课程
            GradeInfo gradeInfo = new GradeInfo();
            gradeInfo.setNeedGrade(String.valueOf(needScore));
            gradeInfo.setAlreadyGrade(String.valueOf(currentScore));
            gradeInfo.setId(id);

            try {
                MyApplication.sDbUtils.deleteAll(GradeInfo.class);
                MyApplication.sDbUtils.save(gradeInfo);
            } catch (DbException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //apk下载
    public static void download(final String urlStr, final SplashActivity splashActivity) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //下载
                InputStream in = null;
                OutputStream out = null;
                byte[] data;
                URL url;
                try {
                    url = new URL(urlStr);
                    in = url.openStream();
                    data = getBytes(in);
                    if (in == null) {
                        throw new Exception("获取下载文件失败！");
                    }
                    String fileType = "apk";
                    String fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1) + ".apk";
                    File localFile = FileUtil.saveFile(fileType, fileName);
                    out = new FileOutputStream(localFile);
                    out.write(data);
                    out.flush();
                    //下载完毕安装
                    splashActivity.installApk(localFile);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }


    //定义一个根据图片url获取InputStream的方法
    public static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // 用数据装
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.close();
        // 关闭流一定要记得。
        return out.toByteArray();
    }


}
