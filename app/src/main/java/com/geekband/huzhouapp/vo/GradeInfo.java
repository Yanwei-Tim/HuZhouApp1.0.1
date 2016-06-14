package com.geekband.huzhouapp.vo;

/**
 * Created by Administrator on 2016/5/30
 */
public class GradeInfo {
    private int id ;
    private String needGrade;
    private String requiredCourse;
    private String electiveCourse;
    private String alreadyGrade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNeedGrade() {
        return needGrade;
    }

    public void setNeedGrade(String needGrade) {
        this.needGrade = needGrade;
    }

    public String getRequiredCourse() {
        return requiredCourse;
    }

    public void setRequiredCourse(String requiredCourse) {
        this.requiredCourse = requiredCourse;
    }

    public String getElectiveCourse() {
        return electiveCourse;
    }

    public void setElectiveCourse(String electiveCourse) {
        this.electiveCourse = electiveCourse;
    }

    public String getAlreadyGrade() {
        return alreadyGrade;
    }

    public void setAlreadyGrade(String alreadyGrade) {
        this.alreadyGrade = alreadyGrade;
    }

    @Override
    public String toString() {
        return "GradeInfo{" +
                "id=" + id +
                ", needGrade='" + needGrade + '\'' +
                ", requiredCourse='" + requiredCourse + '\'' +
                ", electiveCourse='" + electiveCourse + '\'' +
                ", alreadyGrade='" + alreadyGrade + '\'' +
                '}';
    }
}
