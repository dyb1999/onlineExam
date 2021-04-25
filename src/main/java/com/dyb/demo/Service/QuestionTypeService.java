package com.dyb.demo.Service;

import com.dyb.demo.Entity.QuestionType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionTypeService {
    //查找三件套
    List<QuestionType> findAll();

    QuestionType findByType(int type);

    List<QuestionType> findByName(String type_name);

    //增
    Integer addQuestionType(QuestionType questionType);

    //删除
    Integer deleteByType(int type);

    //修改题型名字
    Integer updateByType(QuestionType questionType);

}
