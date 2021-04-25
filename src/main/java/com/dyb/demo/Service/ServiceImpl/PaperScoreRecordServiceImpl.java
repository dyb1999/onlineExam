package com.dyb.demo.Service.ServiceImpl;

import com.dyb.demo.Entity.PaperScoreRecord;
import com.dyb.demo.Entity.Points;
import com.dyb.demo.Entity.Question;
import com.dyb.demo.Entity.Question_Points;
import com.dyb.demo.Mapper.QuestionMapper;
import com.dyb.demo.Mapper.Question_PointsMapper;
import com.dyb.demo.Service.PaperScoreRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaperScoreRecordServiceImpl implements PaperScoreRecordService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    Question_PointsMapper question_pointsMapper;

    //计算不同题型得分率
    @Override
    public Map<Integer, Double> getQuestionTypeRate(List<PaperScoreRecord> paperScoreRecordList) {
        Map<Integer, Integer> Type_Number = new HashMap<>();
        Map<Integer, Integer> Type_CorrectType = new HashMap<>();
        Map<Integer, Double> resultMap = new HashMap<>();
        for (PaperScoreRecord paperScoreRecord : paperScoreRecordList) {
            Question question = questionMapper.findById(paperScoreRecord.getQuestionid());
            //所有题型数量统计
            if (Type_Number.size() != 0) {
                if (!Type_Number.containsKey(question.getType())) {
                    Type_Number.put(question.getType(), 1);//若题型不存在则新增map并设置计数为1
                } else {
                    int value = Type_Number.get(question.getType()).intValue();
                    Type_Number.put(question.getType(), value + 1);
                }
            } else {
                Type_Number.put(question.getType(), 1);
            }

            //所有题型正确数量统计
            if (Type_CorrectType.size() != 0) {
                if (!Type_CorrectType.containsKey(question.getType())) {
                    if (paperScoreRecord.getGetscore() != 0) {
                        Type_CorrectType.put(question.getType(), 1);
                    } else {
                        Type_CorrectType.put(question.getType(), 0);
                    }
                } else if (paperScoreRecord.getGetscore() != 0) {//包含此题型且答题记录正确
                    int value2 = Type_CorrectType.get(question.getType()).intValue();
                    Type_CorrectType.put(question.getType(), value2 + 1);
                }
            } else if (paperScoreRecord.getGetscore() != 0) {
                Type_CorrectType.put(question.getType(), 1);
            } else if (paperScoreRecord.getGetscore() == 0) {
                Type_CorrectType.put(question.getType(), 0);
            }
        }

        //计算正确率
        for (Integer type : Type_Number.keySet()) {
            for (Integer type1 : Type_CorrectType.keySet()) {
                if (type == type1) {
                    double correctNumber = Type_CorrectType.get(type).doubleValue();//转换成整型
                    int totalNumber = Type_Number.get(type).intValue();
                    double result = correctNumber / totalNumber;
                    resultMap.put(type, result);
                }
            }
        }
        return resultMap;
    }

    //计算知识点得分率
    @Override
    public Map<Integer, Double> getPointCorrectRate(List<PaperScoreRecord> paperScoreRecordList) {
        Map<Integer, Integer> PointId_Number = new HashMap<>();
        Map<Integer, Integer> PointId_CorrectNumber = new HashMap<>();
        Map<Integer, Double> resultMap = new HashMap<>();//结果集合《知识点，正确率》
        for (PaperScoreRecord paperScoreRecord : paperScoreRecordList) {
            Question question = questionMapper.findById(paperScoreRecord.getQuestionid());
            List<Integer> points = question_pointsMapper.findByQuestionId(question.getId());//取得知识点集合
            for (Integer point : points) {
                if (PointId_Number.size() != 0) {
                    if (!PointId_Number.containsKey(point)) {
                        PointId_Number.put(point, 1);
                    } else {
                        int value = PointId_Number.get(point);
                        PointId_Number.put(point, value + 1);
                    }
                } else {
                    PointId_Number.put(point, 1);
                }

                int getScore = paperScoreRecord.getGetscore();//

                if (PointId_CorrectNumber.size() != 0) {
                    if (!PointId_CorrectNumber.containsKey(point)) {
                        if (getScore != 0) {
                            PointId_CorrectNumber.put(point, 1);
                        } else {
                            PointId_CorrectNumber.put(point, 0);//当前记录对应题目的题型在map的键值集合里不存在且此记录得分为0则创建题型记录但是设置值为0
                        }
                    } else if (getScore != 0) {//已经有此类题型，并且当前这道题记录得分不为0，则记录+1
                        int value2 = PointId_CorrectNumber.get(point);
                        PointId_CorrectNumber.put(point, value2 + 1);
                    }
                } else if (getScore != 0) {
                    PointId_CorrectNumber.put(point, 1);
                } else if (getScore == 0) {
                    PointId_CorrectNumber.put(point, 0);
                }
            }

        }

        //计算正确率
        for (Integer pointId : PointId_Number.keySet()) {
            for (Integer pointId2 : PointId_CorrectNumber.keySet()) {
                if (pointId == pointId2) {
                    double correctNumber = PointId_CorrectNumber.get(pointId2).doubleValue();
                    int totalNumber = PointId_Number.get(pointId).intValue();
                    double result = correctNumber / totalNumber;
                    resultMap.put(pointId, result);
                }
            }
        }
        return resultMap;
    }
}
