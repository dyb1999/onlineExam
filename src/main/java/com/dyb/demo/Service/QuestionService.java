package com.dyb.demo.Service;

import com.dyb.demo.Entity.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {
    //查找所有题目并显示
    List<Question> findAll();

    //添加题目
    Integer addQuestion(Question question, List<Integer> points);

    //根据id查找
    Question findById(int id);

    //模糊查找
    List<Question> findBySubjectMajor(String subject, String major);

    //删除
    Integer deleteById(int id);

    //根据id修改题目信息
    Integer updateById(Question question);

}
