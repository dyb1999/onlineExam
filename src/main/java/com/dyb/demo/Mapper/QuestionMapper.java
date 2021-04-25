package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface QuestionMapper {
    //, #{choiceA}, #{choiceB}, #{choiceC), #{choiceD}
    @Insert("INSERT INTO t_questions(major, subject, userid, type, question, score, difficulty, answer, choiceA, choiceB, choiceC, choiceD) VALUES (#{major}, #{subject}, #{userid}, #{type}, #{question}, #{score}, #{difficulty} ,#{answer}, #{choiceA}, #{choiceB}, #{choiceC}, #{choiceD})")
    Integer addQuestion(Question question);

    @Select("select * from t_questions")
    List<Question> findAll();

    @Select("select * from t_questions where id=#{id}")
    Question findById(int id);

    @Select("select max(id) from t_questions")
    Integer findMaxIndex();

    //根据专业或者课程名模糊查找题目
    @Select("select * from t_questions where subject like '%${subject}%' or major like '%${major}%")
    List<Question> findBySubjectMajor(String subject, String major);

    @Delete("delete from t_questions where id=#{id}")
    Integer deleteById(int id);

    @Update("update t_questions set major=#{major}, subject=#{subject}, userid=#{userid}, type=#{type}, question=#{question}, score=#{score}, difficulty=#{difficulty}, answer=#{answer} where id=#{id}")
    Integer updateById(Question question);
}
