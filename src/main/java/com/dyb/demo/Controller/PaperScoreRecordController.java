package com.dyb.demo.Controller;

import com.dyb.demo.Entity.PaperScoreRecord;
import com.dyb.demo.Entity.Question;
import com.dyb.demo.Entity.Score;
import com.dyb.demo.Mapper.PaperMapper;
import com.dyb.demo.Mapper.PaperScoreRecordMapper;
import com.dyb.demo.Mapper.QuestionMapper;
import com.dyb.demo.Mapper.ScoreMapper;
import com.dyb.demo.Service.PaperScoreRecordService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/record")
public class PaperScoreRecordController {
    @Autowired
    PaperScoreRecordMapper paperScoreRecordMapper;

    @Autowired
    PaperScoreRecordService paperScoreRecordService;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    PaperMapper paperMapper;

    @Autowired
    ScoreMapper scoreMapper;

    @GetMapping("/list1")
    public String findAll(Model model) {
        List<PaperScoreRecord> paperScoreRecordList = paperScoreRecordMapper.findAll();
        model.addAttribute("recordList", paperScoreRecordList);
        return "paperScoreRecordView";
    }

    //查看某个学生的某场考试某张卷子的所有题目记录
    @PostMapping("/select/student")
    public String selectStudentRecord(@RequestParam("examcode") int examcode, @RequestParam("paperid") int paperid, @RequestParam("loginNumber") String loginNumber, Model model) {
        List<PaperScoreRecord> paperScoreRecordList = paperScoreRecordMapper.findByNumber(loginNumber, examcode, paperid);
        model.addAttribute("recordList", paperScoreRecordList);
        return "studentQuestionRecord";
    }

    //查看试卷的知识点和题型的得分情况-题型
    @PostMapping("/check/condition/questionType")
    public String checkRecordCondition1(@RequestParam("examcode") int examcode, @RequestParam("paperid") int paperid, @RequestParam("loginNumber") String loginNumber, Model model) {
        List<PaperScoreRecord> paperScoreRecordList = paperScoreRecordMapper.findByNumber(loginNumber, examcode, paperid);
        //统计各题型正确率
        Map<Integer, Double> map = paperScoreRecordService.getQuestionTypeRate(paperScoreRecordList);
        Set<Integer> types = map.keySet();
        Integer[] typeArray = new Integer[types.size()];
        Double[] rateArray = new Double[map.values().size()];
        int count = 0;
        for (Integer type : types) {
            typeArray[count++] = type;
        }
        count = 0;
        for (Double rate : map.values()) {
            rateArray[count++] = rate;
        }
        model.addAttribute("types", typeArray);
        model.addAttribute("rates", rateArray);
        return "checkTypesRate";
    }

    //查看试卷的知识点和题型的得分情况-知识点
    @PostMapping("/check/condition/points")
    public String checkRecordCondition2(@RequestParam("examcode") int examcode, @RequestParam("paperid") int paperid, @RequestParam("loginNumber") String loginNumber, Model model) {
        List<PaperScoreRecord> paperScoreRecordList = paperScoreRecordMapper.findByNumber(loginNumber, examcode, paperid);
        //统计各知识点正确率
        Map<Integer, Double> map = paperScoreRecordService.getPointCorrectRate(paperScoreRecordList);
        Set<Integer> pointIds = map.keySet();//知识点编号集合
        Integer[] points = new Integer[pointIds.size()];//
        Double[] rates = new Double[map.values().size()];//
        int count = 0;
        for (Integer pointId : pointIds) {
            points[count++] = pointId;
        }
        count = 0;
        for (Double rate : map.values()) {
            rates[count++] = rate;
        }
        model.addAttribute("points", points);
        model.addAttribute("rates", rates);
        return "checkPointsRate";
    }

    //学生提交一道题答案
    @PostMapping("/submit")
    public String submitQuestionAnswerRecord(PaperScoreRecord paperScoreRecord, Model model) {
        String loginNumber = SecurityUtils.getSubject().getPrincipal().toString();
        paperScoreRecord.setLoginNumber(loginNumber);
        paperScoreRecordMapper.updateRecordAnswer(paperScoreRecord);
        return null;
    }


    //老师更新题目得分(学生的loginNumber,examcode,paperid,questionid,学生答案为空，getScore)
    @PostMapping("/judgeScore")
    public String judgeScore(PaperScoreRecord paperScoreRecord) {
        int score = paperScoreRecord.getGetscore();//老师给分
        int questionid = paperScoreRecord.getQuestionid();
        int fullScore = questionMapper.findById(questionid).getScore();//题目满分
        //检查给分区间
        if (score >= 0 && score <= fullScore) {
            paperScoreRecordMapper.updateRecordScore(paperScoreRecord);//更新题目得分
        }
        if (score < 0) {
            paperScoreRecord.setGetscore(0);
            paperScoreRecordMapper.updateRecordScore(paperScoreRecord);
        }
        return "redirect:/record/list1";
    }

    //添加答题记录
    //@RequestParam("examcode") int examcode, @RequestParam("paperid") int paperid, @RequestParam("questionid") int questionid, @RequestParam("loginNumber") String loginNumber,
    @GetMapping("/add")
    public String addNewQuestionRecord() {
        return "addNewRecord";
    }

    @PostMapping("/add")
    public String addNewQuestionRecord(PaperScoreRecord paperScoreRecord) {
        paperScoreRecordMapper.addRecord(paperScoreRecord);
        return "redirect:/record/list1";
    }

    //删除一个学号对应的所有答题记录，可不用，逻辑应当为删除总成绩，然后级联删除每道题的记录
    @PostMapping("/delete")
    public String deleteById(@RequestParam("loginNumber") String loginNumber) {
        paperScoreRecordMapper.deleteByNumber(loginNumber);
        return "redirect:/record/list1";
    }

    //自动判断所有答题记录中指定题型的得分,忽略大小写
    @PostMapping("/autoGenerate")
    public String autoGetByQuestionType(@RequestParam("type") int type) {
        List<PaperScoreRecord> paperScoreRecordList = paperScoreRecordMapper.findAll();
        for (PaperScoreRecord paperScoreRecord : paperScoreRecordList) {
            int questionid = paperScoreRecord.getQuestionid();
            Question question = questionMapper.findById(questionid);
            int questiontype = question.getType();
            if (questiontype == type) {
                String answer = question.getAnswer();
                String studentanswer = paperScoreRecord.getStudentanswer();
                int fullscore = question.getScore();
                if (studentanswer == null) {//增加判空处理
                    paperScoreRecord.setGetscore(0);
                    paperScoreRecordMapper.updateRecordScore(paperScoreRecord);
                } else if (studentanswer.equalsIgnoreCase(answer)) {
                    paperScoreRecord.setGetscore(fullscore);
                    paperScoreRecordMapper.updateRecordScore(paperScoreRecord);
                }
            }
        }
        return "redirect:/record/list1";
    }
}
