package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.QuestionType;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface QuestionTypeMapper {

    @Select("select * from t_question_type")
    List<QuestionType> findAll();

    @Select("select * from t_question_type where type=#{type}")
    QuestionType findByType(int type);

    @Select("select * from t_question_type where type_name like '%${type_name}%'")
    List<QuestionType> findByName(String type_name);

    @Insert("insert into t_question_type(type,type_name) values (#{type},#{type_name})")
    Integer addQuestionType(QuestionType questionType);

    @Delete("delete from t_question_type where type=#{type}")
    Integer deleteByType(int type);

    @Update("update t_question_type set type_name=#{type_name} where type=#{type}")
    Integer updateByType(QuestionType questionType);
}
