package com.dyb.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    private int examcode;
    private int paperid;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String examname;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date starttime;
    private Date endtime;
    private String exammajor;
    private int examgrade;
    private int examclass;
    private String examinfo;

    public int getExamcode() {
        return examcode;
    }

    public void setExamcode(int examcode) {
        this.examcode = examcode;
    }

    public int getPaperid() {
        return paperid;
    }

    public void setPaperid(int paperid) {
        this.paperid = paperid;
    }

    public String getExamname() {
        return examname;
    }

    public void setExamname(String examname) {
        this.examname = examname;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getExammajor() {
        return exammajor;
    }

    public void setExammajor(String exammajor) {
        this.exammajor = exammajor;
    }

    public int getExamgrade() {
        return examgrade;
    }

    public void setExamgrade(int examgrade) {
        this.examgrade = examgrade;
    }

    public int getExamclass() {
        return examclass;
    }

    public void setExamclass(int examclass) {
        this.examclass = examclass;
    }
}
