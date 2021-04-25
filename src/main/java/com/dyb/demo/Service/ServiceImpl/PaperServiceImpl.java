package com.dyb.demo.Service.ServiceImpl;

import com.dyb.demo.Entity.Paper;
import com.dyb.demo.Entity.Paper_Question;
import com.dyb.demo.Entity.Question;
import com.dyb.demo.Mapper.PaperMapper;
import com.dyb.demo.Mapper.Paper_QuestionMapper;
import com.dyb.demo.Service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaperServiceImpl implements PaperService {
    @Autowired
    PaperMapper paperMapper;

    @Autowired
    Paper_QuestionMapper paper_questionMapper;

    //存试卷和对应试题表
    @Override
    public Integer savePaperRelation(Paper paper) {
        paperMapper.addPaper(paper);
        Integer maxIndex = paperMapper.findMaxIndex();
        for (Question question : paper.getQuestionList()) {
            Paper_Question paper_question = new Paper_Question();
            //此处有问题,如果当前试卷列表中最大的id编号对应的试卷不是新插入的试卷，或者出现多线程并发存试卷，有可能导致错误的编号
            paper_question.setPaper_id(maxIndex);
            paper_question.setQuestion_id(question.getId());
            paper_questionMapper.addPaper_Question(paper_question);
        }
        return null;
    }

}
