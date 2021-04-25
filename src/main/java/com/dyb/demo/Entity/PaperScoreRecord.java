package com.dyb.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperScoreRecord {
    //考试成绩记录
    private int id;
    private String loginNumber;
    private int examcode;
    private int paperid;
    private int questionid;
    //题目得分
    private int getscore;
    //学生答案
    private String studentanswer;

    public String getStudentanswer() {
        return studentanswer;
    }

    public void setStudentanswer(String studentanswer) {
        this.studentanswer = studentanswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginNumber() {
        return loginNumber;
    }

    public void setLoginNumber(String loginNumber) {
        this.loginNumber = loginNumber;
    }

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

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public int getGetscore() {
        return getscore;
    }

    public void setGetscore(int getscore) {
        this.getscore = getscore;
    }
}
