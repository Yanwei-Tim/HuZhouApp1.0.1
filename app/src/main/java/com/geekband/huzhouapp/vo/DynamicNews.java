package com.geekband.huzhouapp.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/25
 */
public class DynamicNews implements Serializable,Parcelable{
    private static final long serialVersionUID = 5116622373292499331L;
    private int id;
    private String title;
    private String date;
    private String writerId;
    private String auditorId;
    private String picUrl;
    private String content;

    public DynamicNews() {
    }

    protected DynamicNews(Parcel in) {
        id = in.readInt();
        title = in.readString();
        date = in.readString();
        writerId = in.readString();
        auditorId = in.readString();
        picUrl = in.readString();
        content = in.readString();
    }

    public static final Creator<DynamicNews> CREATOR = new Creator<DynamicNews>() {
        @Override
        public DynamicNews createFromParcel(Parcel in) {
            return new DynamicNews(in);
        }

        @Override
        public DynamicNews[] newArray(int size) {
            return new DynamicNews[size];
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(writerId);
        dest.writeString(auditorId);
        dest.writeString(picUrl);
        dest.writeString(content);
    }

    @Override
    public String toString() {
        return "DynamicNews{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", writerId='" + writerId + '\'' +
                ", auditorId='" + auditorId + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
