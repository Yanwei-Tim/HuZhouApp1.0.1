package com.geekband.huzhouapp.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/25
 */
public class LocalNews implements Serializable{
    private static final long serialVersionUID = 5116622373292499331L;
    private int id;
    private String title;
    private String picUrl;
    private String content;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "LocalNews{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
