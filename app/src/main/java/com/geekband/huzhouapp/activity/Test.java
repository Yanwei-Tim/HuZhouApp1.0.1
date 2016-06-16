package com.geekband.huzhouapp.activity;

import com.database.pojo.NewsTable;
import com.geekband.huzhouapp.vo.LocalNews;
import com.net.post.DocInfor;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/15
 */
public class Test {
    /**
     * @return 本地新闻信息
     */

    public static ArrayList<LocalNews> getLocalNewsList() {
        ArrayList<LocalNews> localNewsList = new ArrayList<>();
        //获取服务器新闻信息
        int pageSize = 10;
        int currentPage = 1;
        //开始位置
        //queryTable(String sql,String size,String orderBy,
        // String columnList,String command,DocInfor docInfor,String offset)
        String  sql = "from " + NewsTable.TABLE_NAME;
        String size = String.valueOf(pageSize);
        String orderBy = "DESC";
        String columnList = "";
        String command = "SEARCHYOUNGCONTENT";
        DocInfor docInfor = new DocInfor(null,NewsTable.TABLE_NAME);
        String offset = String.valueOf(pageSize*currentPage);
//        ArrayList<BaseTable> baseTables = DataOperation.queryTable(sql,size,orderBy,columnList,command,docInfor,offset);
//
//        ArrayList<NewsTable> newsList = DataOperation.queryNewsTableList();
//        if (newsList != null) {
//            for (int i = 0; i < newsList.size(); i++) {
//                LocalNews localNews = new LocalNews();
//                String title = newsList.get(i).getRecord().getField(NewsTable.FIELD_TITLE);
//                ArrayList<String> picUrlList = (ArrayList<String>) newsList.get(i).getAccessaryFileList();
//                String picUrl = null;
//                if (picUrlList.size() != 0) {
//                    picUrl = picUrlList.get(0);
//                }
//                String contentID = newsList.get(i).getContentId();
//                String date = newsList.get(i).getRecord().getField(NewsTable.FIELD_DATETIME);
//
//                //根据contentId获取新闻内容
//                ContentTable contentTable = DataOperation.queryContentTable(contentID);
//                if (contentTable != null) {
//                    String content = contentTable.getRecord().getField(ContentTable.FIELD_SUBSTANCE);
//                    //将获取的新闻信息放入本地LocalNews
//                    localNews.setTitle(title);
//                    localNews.setPicUrl(picUrl);
//                    localNews.setDate(date);
//                    localNews.setContent(content);
//                    localNews.setId(i);
//                }
//                localNewsList.add(localNews);
//            }
//        }

        return localNewsList;
    }


}
