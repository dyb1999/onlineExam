package com.dyb.demo.Controller;


import com.dyb.demo.Entity.Paper;
import com.dyb.demo.Entity.Paper_Question;
import com.dyb.demo.Entity.Question;
import com.dyb.demo.Mapper.*;
import com.dyb.demo.Service.PaperService;
import com.dyb.demo.Service.ServiceImpl.Paper_QuestionServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.tree.Tree;

import java.util.*;

@Controller
@RequestMapping("/paper")
public class PaperController {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    Question_PointsMapper question_pointsMapper;

    @Autowired
    PaperMapper paperMapper;

    @Autowired
    Paper_QuestionMapper paper_questionMapper;

    @Autowired
    Paper_QuestionServiceImpl paper_questionService;

    @Autowired
    PaperService paperService;

    @GetMapping("/list")
    public String findAll(Model model) {
        List<Paper> paperList = paperMapper.findAll();
        model.addAttribute("paperList", paperList);
        return "paperList";
    }

    //TODO-试卷分析

    @GetMapping("/view")
    public String getView(@RequestParam("paperid") int paperid, Model model) {
        Paper paper = paperMapper.findById(paperid);
        List<Question> questionList = new ArrayList<>();
        List <Paper_Question> paper_questions= paper_questionMapper.findByPaperId(paperid);
        for (Paper_Question paper_question : paper_questions) {
            Question question = questionMapper.findById(paper_question.getQuestion_id());
            questionList.add(question);
        }
        paper.setQuestionList(questionList);
        model.addAttribute("paper", paper);
        return "paper";
    }

    //0point代表知识点json集合
    @PostMapping("/test")
    @ResponseBody
    public String testArrayList(@RequestBody Map<String, String> mapList) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String result = objectMapper.writeValueAsString(mapList);
        Map<Integer, Integer> mapList2 = new TreeMap<>();
        for (Map.Entry<String, String> entry : mapList.entrySet()) {
            if (!entry.getKey().equals("point")) {
                mapList2.put(Integer.valueOf(entry.getKey()), Integer.valueOf(entry.getValue()));
            }

        }
        String[] points = mapList.get("point").split(",");
        System.out.println(mapList);
        System.out.println(mapList2);
        for (String point : points) {
            System.out.println(point);
        }
        return "success";
    }

    //int totalScore, double difficulty, String points, double expect, @RequestBody TreeMap<Integer, Integer> map
//int totalScore, double difficulty,List<Integer> points, double expect, TreeMap<Integer, Integer> map
    //待解决-前端传来的json格式转化为数组或集合，@RequestBody
    //待修改，这里totalscore应该可以不由前端传，应当在前端选定各个题型数量后，由后端/?前端/自动计算总分值
    @RequestMapping("/generate")
    public String generatePaper(Model model, @RequestBody Map<String, String> mapList, @RequestParam("totalScore") int totalScore, @RequestParam("difficulty") double difficulty, @RequestParam("expect") double expect) {
        TreeMap<Integer, Integer> mapList2 = new TreeMap<>();
        for (Map.Entry<String, String> entry : mapList.entrySet()) {
            if (!entry.getKey().equals("point")) {
                mapList2.put(Integer.valueOf(entry.getKey()), Integer.valueOf(entry.getValue()));
            }

        }
        String[] points = mapList.get("point").split(",");
        List<Integer> points2 = new ArrayList<>();
        for (String point : points) {
            points2.add(Integer.valueOf(point));
        }
        Paper paper = paper_questionService.generate(totalScore, difficulty, points2, expect, mapList2);
        model.addAttribute("paper", paper);//待修改为paper而不是paper.questionList
        paperService.savePaperRelation(paper);//保存到数据库中
        return "paper";
    }

    //手动添加试卷，json类型参数（科目名字，题目数组）示例如下
    //    {
    //        "subject":"微积分",
    //        "questionlist":"1520,1387,1315,1345,1367"
    //    }
    @PostMapping("/add")
    public String addPaper(@RequestBody Map<String, String> paramMap, Model model) {
        Paper paper = new Paper();
        int totalScore = 0;
        List<Paper_Question> paper_questions = new ArrayList<>();
        List<Question> questionList = new ArrayList<>();
        List<Integer> questions3 = new ArrayList<>();
        for (Map.Entry<String, String> map : paramMap.entrySet()) {
            paper.setPaperid(paperMapper.findMaxIndex() + 1);
            if (map.getKey().equals("subject")) {
                paper.setSubject(map.getValue());
            }
            if (map.getKey().equals("questionlist")) {
                String questions = map.getValue();
                String[] questions2 = questions.split(",");
                for (String question : questions2) {
                    questions3.add(Integer.valueOf(question));
                }
                paper.setProblemCount(questions2.length);
            }
        }
        for (Integer questionid : questions3) {
            Question question = questionMapper.findById(questionid);
            totalScore += question.getScore();
            questionList.add(question);
        }
        double difficulty = Paper_QuestionServiceImpl.getDifficulty(questionList);
        paper.setTotalscore(totalScore);
        paper.setQuestionList(questionList);
        paper.setDifficulty(difficulty);
        paperService.savePaperRelation(paper);
        model.addAttribute("paper", paper);
        return "paper";
    }
}
