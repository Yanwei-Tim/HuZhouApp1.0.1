package com.geekband.huzhouapp.vo;

/**
 * Created by Administrator on 2016/5/16.
 */
public class ClassInfo {
    private int imageId;
    private String classTitle;

    public ClassInfo(int imageId, String classTitle) {
        this.imageId = imageId;
        this.classTitle = classTitle;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }
}
