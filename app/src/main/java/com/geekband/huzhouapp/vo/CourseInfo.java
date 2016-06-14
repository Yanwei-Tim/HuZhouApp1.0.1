package com.geekband.huzhouapp.vo;

/**
 * Created by Administrator on 2016/5/27.
 */
public class CourseInfo {
    private int id;
    //课程名
    private String title;
    //选修必修
    private String type;
    //课程简介
    private String intro;
    //详细内容
    private String point;
    //学习时长
    private String time;
    //详细内容
    private String detailed;

    public String getDetailed() {
        return detailed;
    }

    public void setDetailed(String detailed) {
        this.detailed = detailed;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", intro='" + intro + '\'' +
                ", point='" + point + '\'' +
                ", time='" + time + '\'' +
                ", detailed='" + detailed + '\'' +
                '}';
    }
}
