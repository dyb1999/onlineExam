package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.Exam;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ExamMapper {
    //考试发布，试卷

    @Select("select * from t_exam")
    List<Exam> findAll();

    @Select("select * from t_exam where examcode=#{examcode}")
    Exam findByExamCode(int examcode);

    //根据专业，班级，年级查询考试场次
    @Select("select * from t_exam where exammajor=#{exammajor},examgrade=#{examgrade},examclass=#{examclass}")
    List<Exam> findByInfo(String exammajor, int examgrade, int examclass);

    //删除考试
    @Delete("delete from t_exam where examcode=#{examcode}")
    Integer deleteByExamCode(int examcode);

    //添加考试
    @Insert("insert into t_exam(paperid,examname,starttime,endtime,exammajor,examgrade,examclass,examinfo) values (#{paperid},#{examname},#{starttime},#{endtime},#{exammajor},#{examgrade},#{examclass},#{examinfo})")
    Integer addExam(Exam exam);

    //更新考试信息
    @Update("update t_exam set paperid=#{paperid},examname=#{examname},starttime=#{starttime},endtime=#{endtime},exammajor=#{exammajor},examgrade=#{examgrade},examclass=#{examclass},examinfo=#{examinfo} where examcode=#{examcode}")
    Integer updateExamInfo(Exam exam);
}
