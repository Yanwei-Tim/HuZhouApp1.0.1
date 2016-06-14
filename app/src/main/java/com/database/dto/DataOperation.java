package com.database.dto;

import com.database.pojo.AlbumTable;
import com.database.pojo.AppVersionTable;
import com.database.pojo.BaseTable;
import com.database.pojo.ContentTable;
import com.database.pojo.CourseTable;
import com.database.pojo.DataSetList;
import com.database.pojo.Document;
import com.database.pojo.ExpertTable;
import com.database.pojo.NewsTable;
import com.database.pojo.ReplyTable;
import com.database.pojo.StudyInfoTable;
import com.database.pojo.StudyScoreTable;
import com.database.pojo.UserInfoTable;
import com.database.pojo.UserTable;
import com.net.post.DocInfor;
import com.net.post.PostHttp;
import com.net.post.XmlPackage;
import com.oa.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class DataOperation {
    private DataOperation() {

    }

    /**
     * 根据用户名查询与之对应的数据
     *
     * @param userName
     * @return
     */
    public static UserTable queryUserTable(String userName) {
        ArrayList<UserTable> userDataList = new ArrayList<UserTable>();
        ArrayList<BaseTable> dataList = queryTable(UserTable.TABLE_NAME, UserTable.FIELD_USERNAME, userName);
        for (BaseTable table : dataList) {
            userDataList.add((UserTable) table);
        }

        if (userDataList.size() != 0) return userDataList.get(0);
        else return null;
    }

    /**
     * 根据 UserTable中的contentId字段 返回与字段在数据库中相匹配的 UserInfo表数据
     *
     * @param contentId
     * @return
     */
    public static UserInfoTable queryUserInfoTable(String contentId) {
        ArrayList<UserInfoTable> userInfoDataList = new ArrayList<UserInfoTable>();
        ArrayList<BaseTable> dataList = queryTable(UserInfoTable.TABLE_NAME, UserInfoTable.FIELD_USERID, contentId);

        for (BaseTable table : dataList) {
            userInfoDataList.add((UserInfoTable) table);
        }

        if (userInfoDataList.size() != 0) return userInfoDataList.get(0);
        else return null;
    }

    /**
     * 从服务器端获取新闻列表数据
     *
     * @return
     */
    public static ArrayList<NewsTable> queryNewsTableList() {
        ArrayList<NewsTable> newsDataList = new ArrayList<NewsTable>();
        ArrayList<BaseTable> dataList = queryTable(NewsTable.TABLE_NAME);

        for (BaseTable table : dataList) {
            newsDataList.add((NewsTable) table);
        }

        if (newsDataList.size() != 0) return newsDataList;
        else return null;
    }

    /**
     * 从服务器端获取专家列表数据
     *
     * @return
     */
    public static ArrayList<ExpertTable> queryExpertTableList() {
        ArrayList<ExpertTable> expertDataList = new ArrayList<ExpertTable>();
        ArrayList<BaseTable> dataList = queryTable(ExpertTable.TABLE_NAME);

        for (BaseTable table : dataList) {
            expertDataList.add((ExpertTable) table);
        }

        if (expertDataList.size() != 0) return expertDataList;
        else return null;
    }

    /**
     * 获取用户的[课程]信息列表
     *
     * @param userNo
     * @return
     */
    public static ArrayList<CourseTable> queryCourseTableList(String userNo) {
        ArrayList<CourseTable> courseDataList = new ArrayList<CourseTable>();
        ArrayList<BaseTable> dataList = queryTable(CourseTable.TABLE_NAME, CourseTable.FIELD_USERNO, userNo);

        for (BaseTable table : dataList) {
            courseDataList.add((CourseTable) table);
        }

        if (courseDataList.size() != 0) return courseDataList;
        else return null;
    }

    /**
     * 获取用户的[回复]信息列表
     *
     * @param userNo
     * @return
     */
    public static ArrayList<ReplyTable> queryReplyTableList(String userNo) {
        ArrayList<ReplyTable> replyDataList = new ArrayList<ReplyTable>();
        ArrayList<BaseTable> dataList = queryTable(ReplyTable.TABLE_NAME, ReplyTable.FIELD_USERNO, userNo);

        for (BaseTable table : dataList) {
            replyDataList.add((ReplyTable) table);
        }

        if (replyDataList.size() != 0) return replyDataList;
        else return null;
    }

    /**
     * 查询一条新闻的内容
     *
     * @param newsId
     * @return
     */
    public static ContentTable queryContentTable(String newsId) {
        ArrayList<ContentTable> contentDataList = new ArrayList<ContentTable>();
        ArrayList<BaseTable> dataList = queryTable(ContentTable.TABLE_NAME, ContentTable.FIELD_NEWSID, newsId);

        for (BaseTable table : dataList) {
            contentDataList.add((ContentTable) table);
        }

        if (contentDataList.size() != 0) return contentDataList.get(0);
        else return null;
    }

    /**
     * 查询用户的相片列表
     *
     * @param userContentId
     * @return
     */
    public static ArrayList<AlbumTable> queryUserAlbumTableList(String userContentId) {
        ArrayList<AlbumTable> albumDataList = new ArrayList<AlbumTable>();
        ArrayList<BaseTable> dataList = queryTable(AlbumTable.TABLE_NAME, AlbumTable.FIELD_USERID, userContentId);

        for (BaseTable table : dataList) {
            albumDataList.add((AlbumTable) table);
        }

        if (albumDataList.size() != 0) return albumDataList;
        else return null;
    }

    public static ArrayList<BaseTable> queryTable(String tableName) {
        String sqlStr = "from " + tableName;
        return queryTable(tableName, sqlStr);
    }

    public static ArrayList<BaseTable> queryTable(String tableName, String fieldName, String fieldValue) {
        String sqlStr = "from " + tableName + " where " + fieldName + "='" + fieldValue + "'";
        return queryTable(tableName, sqlStr);
    }


    /**
     * 执行sql查询语句，查询指定的表
     *
     * @param tableName
     * @param sqlStr
     * @return
     */
    public static ArrayList<BaseTable> queryTable(String tableName, String sqlStr) {
        DataSetList resultData = null;
        ArrayList<BaseTable> tableDataList = new ArrayList<BaseTable>();

        String xmlStr = XmlPackage.packageSelect(sqlStr, "", "", "", "SEARCHYOUNGCONTENT", new DocInfor("", tableName), true, false);
        try {
            resultData = PostHttp.PostXML(xmlStr);
            tableDataList = DataParser.getTable(resultData, tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tableDataList;
    }

    /**
     * 根据用户contentId查询与之对应的数据
     *
     * @param contentId
     * @return
     */
    public static UserTable queryUserTableFormContentId(String contentId) {
        ArrayList<UserTable> userDataList = new ArrayList<UserTable>();
        ArrayList<BaseTable> dataList = queryTable(UserTable.TABLE_NAME, BaseTable.CONTENTID, contentId);
        for (BaseTable table : dataList) {
            userDataList.add((UserTable) table);
        }

        if (userDataList.size() != 0) return userDataList.get(0);
        else return null;
    }


    /**
     * 向服务器端 插入一条表记录/更新现有的一条记录
     * @param tableData 要 插入/更新 的表记录
     * @param file 附件(为null时，表示不包含附件)
     * @return 插入成功的表记录的contentId
     */
    public static String insertOrUpdateTable(BaseTable tableData, Document file)
    {
        String contentId = null;

        String xmlStr = "";
        if(file==null) //若无附件
        {
            xmlStr = XmlPackage.packageForSaveOrUpdate(
                    (HashMap<?, ?>) tableData.getRecord().getFieldList(),
                    new DocInfor(tableData.getContentId(), tableData.getTableName()),  //当该表记录的contentId对应数据库中的一条已有的表记录时，更新该条记录；不对应已有记录时，添加新记录
                    false);
        }
        else //若有附件
        {
            xmlStr = XmlPackage.packageForInsertFileData(
                    (HashMap<?, ?>) tableData.getRecord().getFieldList(),
                    new DocInfor(tableData.getContentId(), tableData.getTableName()),
                    true,
                    file.getPath(),
                    file.getFileType());
        }

        try
        {
            DataSetList resultData = PostHttp.PostXML(xmlStr);
            if(Constants.REQUEST_RESULT_SUCCESS.equals(resultData.SUCCESS)) contentId = resultData.CONTENTID.get(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return contentId;
    }

    /**
     * 删除服务器上的一条表记录
     *
     * @param tableData
     * @return 删除成功或失败
     */
    public static boolean deleteTable(BaseTable tableData) {
        boolean result = false;

        String xmlStr = XmlPackage.packageDelete(tableData.getContentId());

        try {
            DataSetList resultData = PostHttp.PostXML(xmlStr);
            if (Constants.REQUEST_RESULT_SUCCESS.equals(resultData.SUCCESS)) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取服务器应用版本列表信息
     *
     * @return
     */
    public static ArrayList<BaseTable> queryAppVersionTableList() {
//        ArrayList<AppVersionTable> appVersionDataList = new ArrayList<AppVersionTable>();
        ArrayList<BaseTable> dataList = queryTable(AppVersionTable.TABLE_NAME);

//        for (BaseTable table : dataList) {
//
//            appVersionDataList.add((AppVersionTable) table);
//        }

        if (dataList.size() != 0){
            return dataList;
        } else return null;
    }


    /**
     * 获取用户的[学分要求]信息列表
     *
     * @param userNo
     * @return
     */
    public static ArrayList<StudyScoreTable> queryStudyScoreTableList(String userNo) {
        ArrayList<StudyScoreTable> studyScoreDataList = new ArrayList<>();
        ArrayList<BaseTable> dataList = queryTable(StudyScoreTable.TABLE_NAME, StudyScoreTable.FIELD_USERNO, userNo);

        for (BaseTable table : dataList) {
            studyScoreDataList.add((StudyScoreTable) table);
        }

        if (studyScoreDataList.size() != 0) return studyScoreDataList;
        else return null;
    }


    /**
     * 查询课程表
     *
     * @param contentId 用户的contentId
     * @return 课程表信息
     */
    public static CourseTable queryCourseTable(String contentId) {
        ArrayList<CourseTable> courseDataList = new ArrayList<>();
        ArrayList<BaseTable> dataList = queryTable(CourseTable.TABLE_NAME, BaseTable.CONTENTID, contentId);

        for (BaseTable table : dataList) {
            courseDataList.add((CourseTable) table);
        }

        if (courseDataList.size() != 0) return courseDataList.get(0);
        else return null;
    }

    /**
     * 获取用户的已修学分
     *
     * @param contentId 用户Id
     * @return 已修学分
     */
    public static int queryUserCurrentScore(String contentId) {
        int score = -1;

        List<StudyInfoTable> studyInfoTableList = queryStudyInfoTableList(contentId);
        if (studyInfoTableList != null) {
            for (StudyInfoTable studyInfoTable : studyInfoTableList) {
                if (studyInfoTable != null) {
                    CourseTable courseTable = queryCourseTable(studyInfoTable.getRecord().getField(StudyInfoTable.FIELD_COURSENO));
                    if (courseTable != null) {
                        score += Integer.parseInt(courseTable.getRecord().getField(CourseTable.FIELD_POINT));
                    }
                }
            }
        }

        return score;
    }

    /**
     * 获取用户的[课程学习情况]信息列表
     *
     * @param userNo
     * @return
     */
    public static ArrayList<StudyInfoTable> queryStudyInfoTableList(String userNo) {
        ArrayList<StudyInfoTable> studyScoreDataList = new ArrayList<>();
        ArrayList<BaseTable> dataList = queryTable(StudyInfoTable.TABLE_NAME, StudyInfoTable.FIELD_USERNO, userNo);

        for (BaseTable table : dataList) {
            studyScoreDataList.add((StudyInfoTable) table);
        }

        if (studyScoreDataList.size() != 0) return studyScoreDataList;
        else return null;
    }

    /**
     * 获取用户当前需修学分
     *
     * @param userNo
     * @return
     */
    public static int queryUserNeedScore(String userNo) {

        int needScore = -1;
        long currentTime = System.currentTimeMillis();

        ArrayList<StudyScoreTable> studyScoreDataList = queryStudyScoreTableList(userNo);
        if (studyScoreDataList != null) {
            for (StudyScoreTable studyScoreTable : studyScoreDataList) {
                if (studyScoreTable != null) {
                    String timePeriod = studyScoreTable.getRecord().getField(StudyScoreTable.FIELD_TIMEPERIOD);
                    String startTimeStr = timePeriod.substring(0, 10);
                    String endTimeStr = timePeriod.substring(11, 21);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        long startTime = sdf.parse(startTimeStr).getTime();
                        long endTime = sdf.parse(endTimeStr).getTime();

                        if (currentTime > startTime && currentTime < endTime) {
                            needScore = Integer.parseInt(studyScoreTable.getRecord().getField(StudyScoreTable.FIELD_NEEDSCORE));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return needScore;
    }


}
