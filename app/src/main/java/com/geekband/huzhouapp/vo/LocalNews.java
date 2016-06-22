package com.geekband.huzhouapp.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/25
 */
public class LocalNews implements Serializable,Parcelable{
    private static final long serialVersionUID = 5116622373292499331L;
    private int id;
    private String title;
    private String picUrl;
    private String content;
    private String date;

    protected LocalNews(Parcel in) {
        id = in.readInt();
        title = in.readString();
        picUrl = in.readString();
        content = in.readString();
        date = in.readString();
    }

    public LocalNews() {
    }

    public static final Creator<LocalNews> CREATOR = new Creator<LocalNews>() {
        @Override
        public LocalNews createFromParcel(Parcel in) {
            return new LocalNews(in);
        }

        @Override
        public LocalNews[] newArray(int size) {
            return new LocalNews[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(picUrl);
        dest.writeString(content);
        dest.writeString(date);
    }
}
