package com.dyb.demo.Service.ServiceImpl;

import com.dyb.demo.Entity.QuestionType;
import com.dyb.demo.Mapper.QuestionTypeMapper;
import com.dyb.demo.Service.QuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionTypeServiceImpl implements QuestionTypeService {
    @Autowired
    QuestionTypeMapper questionTypeMapper;

    @Override
    public List<QuestionType> findAll() {
        return questionTypeMapper.findAll();
    }

    @Override
    public QuestionType findByType(int type) {
        return questionTypeMapper.findByType(type);
    }

    @Override
    public List<QuestionType> findByName(String type_name) {
        return questionTypeMapper.findByName(type_name);
    }

    @Override
    public Integer addQuestionType(QuestionType questionType) {
        return questionTypeMapper.addQuestionType(questionType);
    }

    @Override
    public Integer deleteByType(int type) {
        return questionTypeMapper.deleteByType(type);
    }

    @Override
    public Integer updateByType(QuestionType questionType) {
        return questionTypeMapper.updateByType(questionType);
    }
}
