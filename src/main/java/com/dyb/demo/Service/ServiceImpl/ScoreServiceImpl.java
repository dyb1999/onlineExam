package com.dyb.demo.Service.ServiceImpl;

import com.dyb.demo.Entity.PaperScoreRecord;
import com.dyb.demo.Entity.Score;
import com.dyb.demo.Mapper.PaperMapper;
import com.dyb.demo.Mapper.PaperScoreRecordMapper;
import com.dyb.demo.Mapper.ScoreMapper;
import com.dyb.demo.Service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    PaperMapper paperMapper;

    @Autowired
    ScoreMapper scoreMapper;

    @Autowired
    PaperScoreRecordMapper paperScoreRecordMapper;

    @Override
    public List<Score> findAll() {
        return scoreMapper.findAll();
    }

    @Override
    public List<String> findAllLoginNumber(int examcode, int paperid) {
        List<String> numbers = paperScoreRecordMapper.findNumbersByExam(examcode, paperid);
        return numbers;
    }

    @Override
    public List<Score> findAllRecordByExamcode(int examcode) {
        List<Score> scores = scoreMapper.findByExamcode(examcode);
        return scores;
    }

    @Override
    public List<Score> findAllRecordByNumber(String loginNumber) {
        List<Score> scores = scoreMapper.findByNumber(loginNumber);
        return scores;
    }

    //排序
    @Override
    public void sortOfScore(int examcode) {
        List<Score> scores = scoreMapper.findByExamcode(examcode);
        int length = scores.size();
        for (int i = length; i >= 2; i--) {
            for (int j = 0; j < i-1; j++) {
                if (scores.get(j).getScore() < scores.get(j + 1).getScore()) {
                    Score temp = scores.get(j+1);
                    scores.set(j + 1, scores.get(j));
                    scores.set(j, temp);
                }
            }
        }
        int levelA = scores.size() * 1 / 10;
        int levelB = scores.size() * 3 / 10;
        int levelC = scores.size() * 6 / 10;
//        int levelD = scores.size();
        for (Score score : scores) {
            if (scores.indexOf(score) <= levelA) {
                score.setLevel("A");
                scoreMapper.updateLevelByEPL(score);//更新成绩等级
            } else if (scores.indexOf(score) <= levelB) {
                score.setLevel("B");
                scoreMapper.updateLevelByEPL(score);//更新成绩等级
            } else if (scores.indexOf(score) <= levelC) {
                score.setLevel("C");
                scoreMapper.updateLevelByEPL(score);//更新成绩等级
            } else {
                score.setLevel("D");
                scoreMapper.updateLevelByEPL(score);//更新成绩等级
            }
        }
    }

    @Override
    public void addScore(int examcode, int paperid, String loginNumber) {//添加成绩记录
        int score = paperScoreRecordMapper.getTotalScore(loginNumber, examcode, paperid);
        String subject = paperMapper.findById(paperid).getSubject();
        Score score1 = new Score();
        score1.setExamcode(examcode);
        score1.setPaperid(paperid);
        score1.setLoginNumber(loginNumber);
        score1.setSubject(subject);
        score1.setScore(score);
        scoreMapper.addScoreRecord(score1);
    }

    @Override
    public void updateScore(int examcode, int paperid, String loginNumber) {//更新成绩，
        int score = paperScoreRecordMapper.getTotalScore(loginNumber, examcode, paperid);
        String subject = paperMapper.findById(paperid).getSubject();
        Score score1 = new Score();
        score1.setExamcode(examcode);
        score1.setPaperid(paperid);
        score1.setLoginNumber(loginNumber);
        score1.setSubject(subject);
        score1.setScore(score);
        scoreMapper.updateScoreByEPL(score1);
    }

    //删除总成绩记录和题目记录
    @Override
    public Integer deleteByNumberAndSubject(String loginNumber, int examcode) {
        int paperid = scoreMapper.findPaperidByExamcode(examcode);
        scoreMapper.deleteScoreRecord(examcode, paperid, loginNumber);
        paperScoreRecordMapper.deleteByNumber(loginNumber);
        return null;
    }
}
