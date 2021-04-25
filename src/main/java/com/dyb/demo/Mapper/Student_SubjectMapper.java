package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.Student_Subject;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface Student_SubjectMapper {
    //根据学生学号查找选课记录
    @Select("select * from student_subject where loginNumber=#{loginNumber}")
    List<Student_Subject> findByNumber(String loginNumber);

    //根据id查找
    @Select("select * from student_subject where id=#{id}")
    Student_Subject findById(int id);

    //增加
    @Insert("insert into student_subject(id, loginNumber, subject_id) values (#{id}, #{loginNumber}, #{subject_id})")
    Integer addStudentSubject(Student_Subject student_subject);

    //根据学号删除
    @Delete("delete from student_subject where loginNumber=#{loginNumber}")
    Integer deleteByNumber(String loginNumber);

    //根据学号和课程名删除
    @Delete("delete from student_subject where loginNumber=#{loginNumber} and subject_id=#{subject_id}")
    Integer deleteByNumberSubject(String loginNumber, int subject_id);

    //修改
    @Update("update student_subject set loginNumber=#{loginNumber}, subject_id=#{subject_id}")
    Integer updateByNumberSubject(String loginNumber, int subject_id);

}
