package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.Paper;
import com.dyb.demo.Entity.Paper_Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PaperMapper {
    @Select("select * from t_paper")
    List<Paper> findAll();

    @Select("select max(paperid) max from t_paper")
    Integer findMaxIndex();

    @Select("select * from t_paper where paperid=#{paperid}")
    Paper findById(int paperid);


    //生成试卷后需要存两个表，试卷信息表t_paper,和试卷-题目信息对应表paper_question
    @Insert("insert into t_paper(paperid, subject,totalscore,KPCoverage,difficulty,adaptationDegree,problemCount) values (#{paperid},#{subject},#{totalscore},#{KPCoverage},#{difficulty},#{adaptationDegree},#{problemCount})")
    Integer addPaper(Paper paper);
    //第二个存表操作
    @Insert("insert into paper_question(paper_id,question_id) values (#{paper_id},#{question_id})")
    Integer addPaperQuestion(Paper_Question paper_question);

    @Delete("delete from t_paper where paperid=#{paperid}")
    Integer deletePaper(int paperid);


    @Update("update t_paper set subject=#{subject},totalscore=#{totalscore},KPCoverage=#{KPCoverage},difficulty=#{difficulty},adaptationDegree=#{adaptationDegree},problemCount=#{problemCount}")
    Integer updatePaper(Paper paper);
}
