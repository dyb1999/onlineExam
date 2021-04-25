package com.dyb.demo.Service.ServiceImpl;

import com.dyb.demo.Entity.Question;
import com.dyb.demo.Mapper.QuestionMapper;
import com.dyb.demo.Mapper.Question_PointsMapper;
import com.dyb.demo.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    Question_PointsMapper question_pointsMapper;

    public List<Question> findAll() {
        return questionMapper.findAll();
    }

    //此处操作应当加锁，限制并发
    @Override
    public Integer addQuestion(Question question, List<Integer> points) {
        questionMapper.addQuestion(question);
        Integer maxIndex = questionMapper.findMaxIndex();
        for (Integer point : points) {
            question_pointsMapper.addQuestionPoint(maxIndex, point);
        }
        return null;
    }

    @Override
    public Question findById(int id) {
        return questionMapper.findById(id);
    }

    @Override
    public List<Question> findBySubjectMajor(String subject, String major) {
        return questionMapper.findBySubjectMajor(subject,major);
    }

    @Override
    public Integer deleteById(int id) {
        return questionMapper.deleteById(id);
    }

    @Override
    public Integer updateById(Question question) {
        return questionMapper.updateById(question);
    }

}
