package com.dyb.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paper {
    //试卷编号，所属科目，总分，知识点分布系数，难度系数，适应度函数
    private int paperid;
    private String subject;
    private int totalscore;
    private double KPCoverage;
    private double difficulty;
    private double adaptationDegree;
    private int problemCount;

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    private List<Question> questionList = new ArrayList<>();//题目集合
    private List<Integer> Points = new ArrayList<>();//知识点编号集合
    private TreeMap<Integer,Integer> eachTypeCount = new TreeMap<>();//每种题目数量<题型编号，数量>

    public List<Integer> getPoints() {
        return Points;
    }

    public void setPoints(List<Integer> points) {
        Points = points;
    }

    public TreeMap<Integer, Integer> getEachTypeCount() {
        return eachTypeCount;
    }

    public void setEachTypeCount(TreeMap<Integer, Integer> eachTypeCount) {
        this.eachTypeCount = eachTypeCount;
    }

    public int getPaperid() {
        return paperid;
    }

    public void setPaperid(int paperid) {
        this.paperid = paperid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore;
    }

    public double getKPCoverage() {
        return KPCoverage;
    }

    public void setKPCoverage(double KPCoverage) {
        this.KPCoverage = KPCoverage;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public double getAdaptationDegree() {
        return adaptationDegree;
    }

    public void setAdaptationDegree(double adaptationDegree) {
        this.adaptationDegree = adaptationDegree;
    }

    public int getProblemCount(Map<Integer, Integer> map) {
        int sum = 0;
        for (Map.Entry entry : map.entrySet()) {
            sum += ((Integer)entry.getValue());
        }
        return sum;
    }

    public int getProblemCount() {
        return problemCount;
    }

    public void setProblemCount(int problemCount) {
        this.problemCount = problemCount;
    }
}
