package com.dyb.demo.Service;

import com.dyb.demo.Entity.Score;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScoreService {

    //找到所有的记录
    List<Score> findAll();

    //找到某场考试有记录的所有学生的学号
    List<String> findAllLoginNumber(int examcode, int paperid);

    //找到某场考试有记录的所有学生的总成绩记录
    List<Score> findAllRecordByExamcode(int examcode);

    //找到某个学号的所有成绩
    List<Score> findAllRecordByNumber(String loginNumber);

    //计算排名划分等级
    void sortOfScore(int examcode);

    //添加成绩记录
    void addScore(int examcode, int paperid, String loginNumber);

    //更新成绩记录
    void updateScore(int examcode, int paperid, String loginNumber);

    //级联删除某个学生的总成绩和每道题的记录
    Integer deleteByNumberAndSubject(String loginNumber, int examcode);
}
