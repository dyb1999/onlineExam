package com.dyb.demo.Service;

import com.dyb.demo.Entity.Paper;
import org.springframework.stereotype.Service;

@Service
public interface PaperService {

    //存试卷信息和试卷-题目对应关系表
    Integer savePaperRelation(Paper paper);
}
