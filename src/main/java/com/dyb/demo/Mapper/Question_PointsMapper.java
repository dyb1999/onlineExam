package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.Points;
import com.dyb.demo.Entity.Question_Points;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface Question_PointsMapper {

    @Select("select * from question_points")
    List<Question_Points> findAll();

    @Select("select point_id from question_points where question_id=#{question_id}")
    List<Integer> findByQuestionId(int question_id);

    //根据问题编号查找返回该问题包含的所有知识点集合
    @Select("select * from question_points where question_id=#{question_id}")
    List<Question_Points> findQPByQuestionId(int question_id);

    @Delete("delete from question_points where id=#{id}")
    Integer deleteById(int id);

    @Update("update question_points set question_id=#{question_id}, point_id=#{point_id} where id=#{id}")
    Integer updateById(Question_Points questionPoints);

    //插入到问题-知识点表中question_points
    @Insert("insert into question_points(question_id,point_id) values (#{question_id},#{point_id})")
    Integer addQuestionPoint(int question_id, int point_id);
}
