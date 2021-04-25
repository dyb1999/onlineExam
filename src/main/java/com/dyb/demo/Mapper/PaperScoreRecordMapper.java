package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.PaperScoreRecord;
import com.dyb.demo.Entity.Score;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PaperScoreRecordMapper {

    @Select("select * from paperscorerecord")
    List<PaperScoreRecord> findAll();

    //获取参加某场考试的所有学生的学号集合
    @Select("select distinct loginNumber from paperscorerecord where examcode=#{examcode} and paperid=#{paperid}")
    List<String> findNumbersByExam(int examcode, int paperid);

    //某个考生的某场考试某份卷子的各个题目的得分情况
    @Select("select * from paperscorerecord where examcode=#{examcode} and paperid=#{paperid} and loginNumber=#{loginNumber}")
    List<PaperScoreRecord> findByNumber(String loginNumber, int examcode, int paperid);

    //获取指定考试指定卷子的某个考生的总成绩
    @Select("select sum(getscore) from paperscorerecord where examcode=#{examcode} and paperid=#{paperid} and loginNumber=#{loginNumber}")
    Integer getTotalScore(String loginNumber, int examcode, int paperid);

    //获取指定examcode的考试的指定paperid的卷子的所有学生的成绩（学号，总成绩）
    @Select("select loginNumber,sum(getscore) score from paperscorerecord where examcode=#{examcode} and paperid=#{paperid} group by loginNumber")
    Map<String, Integer> getExamEveryStudentScore(int examcode, int paperid);

    //根据学号删除题目得分记录
    @Delete("delete from paperscorerecord where loginnumber=#{loginnumber}")
    Integer deleteByNumber(String loginnumber);

    //添加每道题的学生答案和得分信息
//    @Insert("insert into paperscorerecord(loginNumber,examcode,paperid,questionid,studentanswer,getscore) values (#{loginNumber},#{examcode},#{paperid},#{questionid},#{studentanswer})")
//    Integer addRecord2(String loginNumber, int examcode, int paperid, int questionid);

    //创建答题记录，证明学生参加考试
    @Insert("insert into paperscorerecord(loginNumber,examcode,paperid,questionid) values (#{loginNumber},#{examcode},#{paperid},#{questionid})")
    Integer addRecord(PaperScoreRecord paperScoreRecord);

    //更新每道题的学生答案和得分信息
    @Update("update paperscorerecord set loginNumber=#{loginNumber}, examcode=#{examcode}, paperid=#{paperid}, questionid=#{questionid}, studentanswer=#{studentanswer}, getscore=#{getscore} where loginNumber=#{loginNumber} and examcode=#{examcode} and paperid=#{paperid} and questionid=#{questionid}")
    Integer updateRecord(PaperScoreRecord paperScoreRecord);

    //更新学生提交答案
    @Update("update paperscorerecord set studentanswer=#{studentanswer} where loginNumber=#{loginNumber} and examcode=#{examcode} and paperid=#{paperid} and questionid=#{questionid}")
    Integer updateRecordAnswer(PaperScoreRecord paperScoreRecord);

    //更新题目得分
    @Update("update paperscorerecord set getscore=#{getscore} where loginNumber=#{loginNumber} and examcode=#{examcode} and paperid=#{paperid} and questionid=#{questionid}")
    Integer updateRecordScore(PaperScoreRecord paperScoreRecord);

    //根据考试编号，试卷编号，题目编号，学生学号查找记录
    @Select("select * from paperscorerecord where examcode=#{examcode} and paperid=#{paperid} and questionid=#{questionid} and loginNumber=#{loginNumber}")
    PaperScoreRecord findByEPQL(PaperScoreRecord paperScoreRecord);
}
