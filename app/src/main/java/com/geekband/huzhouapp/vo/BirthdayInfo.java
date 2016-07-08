package com.geekband.huzhouapp.vo;

/**
 * Created by Administrator on 2016/6/27
 */
public class BirthdayInfo {
    private String avatarImage ;
    private String realName;
    private String date;

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "BirthdayInfo{" +
                "avatarImage='" + avatarImage + '\'' +
                ", realName='" + realName + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
