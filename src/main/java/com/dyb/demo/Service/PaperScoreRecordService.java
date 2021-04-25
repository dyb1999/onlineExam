package com.dyb.demo.Service;

import com.dyb.demo.Entity.PaperScoreRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PaperScoreRecordService {

    //统计各题型数量同时计算正确数量(题型编号，正确率)
    Map<Integer, Double> getQuestionTypeRate(List<PaperScoreRecord> paperScoreRecordList);

    //统计试卷包含的所有知识点和每个知识点的正确率
    Map<Integer, Double> getPointCorrectRate(List<PaperScoreRecord> paperScoreRecordList);
}
