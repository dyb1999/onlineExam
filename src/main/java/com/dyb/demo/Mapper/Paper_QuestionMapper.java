package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.Paper_Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface Paper_QuestionMapper {

    @Select("select * from paper_question")
    List<Paper_Question> findAll();

    @Select("select * from paper_question where paper_id=#{paper_id}")
    List<Paper_Question> findByPaperId(int paper_id);

    @Insert("insert into paper_question(paper_id,question_id) values (#{paper_id},#{question_id})")
    Integer addPaper_Question(Paper_Question paper_question);

    @Delete("delete from paper_question where paper_id=#{paper_id}")
    Integer deletePaper_Question(int paper_id);

    @Update("update paper_question set paper_id=#{paper_id},question_id=#{question_id}")
    Integer updatePaper_Question(Paper_Question paper_question);
}
