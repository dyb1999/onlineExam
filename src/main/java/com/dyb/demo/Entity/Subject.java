package com.dyb.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    //编号，课程名，所属专业，课程所在年级
    private int id;
    private String coursename;
    private String major;
    private int coursegrade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getCoursegrade() {
        return coursegrade;
    }

    public void setCoursegrade(int coursegrade) {
        this.coursegrade = coursegrade;
    }
}
