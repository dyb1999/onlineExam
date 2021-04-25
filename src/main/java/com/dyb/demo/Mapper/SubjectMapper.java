package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.Subject;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SubjectMapper {
    //增加课程
    @Insert("insert into t_subject(coursename,major,coursegrade) values (#{coursename}, #{major}, #{coursegrade})")
    Integer addSubject(Subject subject);

    //删除课程
    @Delete("delete from t_subject where id=#{id}")
    Integer deleteSubjectById(int id);

    //修改课程
    @Update("update t_subject set coursename=#{coursename}, major=#{major}, coursegrade=#{coursegrade} where id=#{id}")
    Integer updateSubjectById(Subject subject);

    //查找所有课程
    @Select("select * from t_subject")
    List<Subject> findAll();

    //根据主键id查找课程
    @Select("select * from t_subject where id=#{id}")
    Subject findById(int id);

    //根据课程名模糊查找
    @Select("select * from t_subject where coursename like '%${coursename}%'")
    List<Subject> findByCourseName(String coursename);
}
