package com.dyb.demo.Controller;

import com.dyb.demo.Entity.*;
import com.dyb.demo.Mapper.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    ExamMapper examMapper;

    @Autowired
    PaperMapper paperMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    Paper_QuestionMapper paper_questionMapper;

    @Autowired
    PaperScoreRecordMapper paperScoreRecordMapper;

    @Autowired
    ScoreMapper scoreMapper;

    @Autowired
    QuestionMapper questionMapper;

    @GetMapping("/list")
    public String findAll(Model model) {
        List<Exam> exams = examMapper.findAll();
        model.addAttribute("exams", exams);
        return "exam";
    }

    @GetMapping("/add")
    public String addExam() {
        return "addExam";
    }

    @PostMapping("/add")
    public String addExam(Exam exam) {
        //TODO-返回待选试卷信息

        examMapper.addExam(exam);
        //此处逻辑应当为添加考试成功后返回所有考试首页
        return "redirect:/exam/list";
    }

    @GetMapping("/delete/{examcode}")
    public String deleteExam(@PathVariable("examcode") int examcode) {
        examMapper.deleteByExamCode(examcode);
        //返回所有试卷首页
        return "redirect:/exam/list";
    }

    @GetMapping("/update/{examcode}")
    public String updateExam(@PathVariable("examcode") int examcode, Model model) {
        Exam exam = examMapper.findByExamCode(examcode);
        model.addAttribute("exam", exam);
        return "updateExam";
    }

    @PostMapping("/update")
    public String updateExam(Exam exam) {
        examMapper.updateExamInfo(exam);
        return "redirect:/exam/list";
    }

    //考生登录，参加考试，需验证身份
    @GetMapping("/enter/{examcode}")
    public String enterExam(@PathVariable("examcode") int examcode, Model model) {
        Exam exam = examMapper.findByExamCode(examcode);
        String loginNumber = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userMapper.getUserByNumber(loginNumber);
        String major = exam.getExammajor();
        int grade = exam.getExamgrade();
        int myclass = exam.getExamclass();
        //判断身份和考试信息是否吻合，在此之前应当在前端判断时间符合才能发请求，后端不做判断
        if (!major.equals(user.getMajor()) || grade != user.getGrade() || myclass != user.getMyclass()) {
            return "redirect:/exam/list";
        }
        //分发试卷,初始化
        int paperid = exam.getPaperid();
        Paper paper = paperMapper.findById(paperid);
        List<Question> questions = new ArrayList<>();
        List<Paper_Question> paper_questions = paper_questionMapper.findByPaperId(paperid);
        for (Paper_Question paper_question : paper_questions) {
            Question question = questionMapper.findById(paper_question.getQuestion_id());
            questions.add(question);
        }
        paper.setQuestionList(questions);//初始化题集
        //创建试卷答题记录,
        Score scoreMapperByEPL = scoreMapper.findByEPL(examcode, paperid, loginNumber);
        if (scoreMapperByEPL == null) {//没有考试记录则创建
            Score score = new Score();
            score.setExamcode(examcode);
            score.setPaperid(paperid);
            score.setLoginNumber(loginNumber);
            score.setSubject(paperMapper.findById(paperid).getSubject());
            scoreMapper.addScoreRecord(score);
            //创建分题答题记录
            List<Question> questionList = paper.getQuestionList();
            for (Question question : questionList) {
                PaperScoreRecord paperScoreRecord = new PaperScoreRecord();
                paperScoreRecord.setLoginNumber(loginNumber);
                paperScoreRecord.setExamcode(examcode);
                paperScoreRecord.setPaperid(paperid);
                paperScoreRecord.setQuestionid(question.getId());
                paperScoreRecord.setStudentanswer(" ");
                paperScoreRecordMapper.addRecord(paperScoreRecord);
            }
        }
        //回填上次答案
        //回填当前考试此学生所有已经提交的记录到卷子里
        List<PaperScoreRecord> paperScoreRecords = paperScoreRecordMapper.findByNumber(loginNumber, examcode, paperid);
        String[] answers = new String[questions.size()];
        int count = 0;
        for (Question question : questions) {
            for (PaperScoreRecord paperScoreRecord : paperScoreRecords) {
                if (question.getId() == paperScoreRecord.getQuestionid()) {
                    answers[count] = paperScoreRecord.getStudentanswer();
                    count++;
                }
            }
        }
        model.addAttribute("answers", answers);
        model.addAttribute("exam", exam);
        model.addAttribute("paper", paper);
        return "ExamPaper";
    }
}
