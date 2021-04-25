package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.Points;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PointsMapper {

    @Select("select * from t_points")
    List<Points> findAll();

    @Insert("insert into t_points(content,subject_id) values (#{content},#{subject_id})")
    Integer addPoints(Points points);

    //插入到问题-知识点表中question_points
    @Insert("insert into question_points(question_id,point_id) values (#{question_id},#{point_id})")
    Integer addQuestionPoint(int question_id,int point_id);
}
