package com.dyb.demo.Controller;

import com.dyb.demo.Entity.Points;
import com.dyb.demo.Entity.Question;
import com.dyb.demo.Service.PointsService;
import com.dyb.demo.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


//题库管理
@Controller
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @Autowired
    PointsService pointsService;

    @GetMapping("/list")
    public String getAll(Model model) {
        List<Question> questions = questionService.findAll();
        model.addAttribute("questions", questions);
        return "questions";
    }

    @GetMapping("/add")
    public String addQuestion(Model model) {
        List<Points> pointsList = pointsService.findAll();
        model.addAttribute("points", pointsList);
        return "addQuestion";
    }

    @PostMapping("/add")
    public String addQuestion(Question question, @RequestParam("points") String points) {
        String[] pointList = points.split(",");
        List<Integer> pointList1 = new ArrayList<>();
        for (String point : pointList) {
            pointList1.add(Integer.valueOf(point));
        }
        questionService.addQuestion(question, pointList1);
        return "redirect:/question/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") int id) {
        questionService.deleteById(id);
        return "redirect:/question/list";
    }

    @GetMapping("/update/{id}")
    public String updateById(@PathVariable("id") int id, Model model) {
        Question question = questionService.findById(id);
        model.addAttribute("question", question);
        return "updateQuestion";
    }

    @PostMapping("/update")
    public String updateById(Question question) {
        questionService.updateById(question);
        return "redirect:/question/list";
    }
    //生成题目测试用,可忽略后期
//    @GetMapping("/generate")
//    @ResponseBody
//    public String generate() {
////        List<Points> points = new ArrayList<>();
////        Random random = new Random();
////        for (int i = 0; i < 100; i++) {
////            Points point = new Points();
////            point.setId(i + 1);
////            point.setContent(String.valueOf(random.nextInt(100) + 1));
////            point.setSubject_id(1);
////            points.add(point);
////        }
////        for (Points point1 : points) {
////            pointsService.addPoints(point1);
////        }
////        return "100个知识点初始化成功！";
//        List<Question> questions = new ArrayList<>();
//        List<Points> pointsList = pointsService.findAll();
//        int size = pointsList.size();
//        Random random = new Random();
//        for (int i = 1; i <= 5000; i++) {
//            Question question = new Question();
//            //1000道选择题（1分）
//            if (i < 1001) {
//                question.setId(i);
//                question.setMajor("软件工程");
//                question.setSubject("微积分");
//                question.setUserid(1);
//                question.setType(1);
//                question.setQuestion(String.valueOf(i + 2));
//                question.setScore(1);//1分
//                question.setDifficulty((random.nextInt(70) + 30) * 0.01);
//                question.setAnswer(String.valueOf(random.nextInt(i + 2)));
//            }
//            //1000道2分选择题
//            if (i > 1000 && i < 2001) {
//                question.setId(i);
//                question.setMajor("软件工程");
//                question.setSubject("微积分");
//                question.setUserid(1);
//                question.setType(2);//
//                question.setQuestion(String.valueOf(i + 2));
//                question.setScore(2);//2分
//                question.setDifficulty((random.nextInt(70) + 30) * 0.01);
//                question.setAnswer(String.valueOf(random.nextInt(i + 2)));
//            }
//            //1000道判断题2分
//            if (i > 2000 && i < 3001) {
//                question.setId(i);
//                question.setMajor("软件工程");
//                question.setSubject("微积分");
//                question.setUserid(1);
//                question.setType(3);//
//                question.setQuestion(String.valueOf(i + 2));
//                question.setScore(2);//2分
//                question.setDifficulty((random.nextInt(70) + 30) * 0.01);
//                question.setAnswer(String.valueOf(random.nextInt(i + 2)));
//            }
//            //1000道填空题1-4分
//            if (i > 3000 && i < 4001) {
//                question.setId(i);
//                question.setMajor("软件工程");
//                question.setSubject("微积分");
//                question.setUserid(1);
//                question.setType(4);//
//                question.setQuestion(String.valueOf(i + 2));
//                question.setScore(random.nextInt(4) + 1);//1-4分
//                question.setDifficulty((random.nextInt(70) + 30) * 0.01);
//                question.setAnswer(String.valueOf(random.nextInt(i + 2)));
//            }
//            //1000道问答题，为难度系数*10
//            if (i > 4000 && i < 5001) {
//                question.setId(i);
//                question.setMajor("软件工程");
//                question.setSubject("微积分");
//                question.setUserid(1);
//                question.setType(4);//
//                question.setQuestion(String.valueOf(i + 2));
//                question.setDifficulty((random.nextInt(70) + 30) * 0.01);
//                question.setScore(question.getDifficulty() > 0.3 ? (int)(question.getDifficulty()*10) : 3);
//                question.setAnswer(String.valueOf(random.nextInt(i + 2)));
//            }
//            questions.add(question);//添加题目
//            questionService.addQuestion(question);
//            System.out.println("这是第" + i + "个题目的插入");
//            //TODO-生成题目包含知识点集合
//            //1-4个知识点
//            int count = random.nextInt(4) + 1;
//            List<Points> pointsList1 = new ArrayList<>();
//            boolean ifHas = false;
//            //            int size = pointsList.size();
//            for (int j = 0; j < count; j++) {
//                Points points2 = pointsList.get(random.nextInt(size));
//                if (j > 0) {
//                    for (Points points3 : pointsList1) {
//                        if (points2.getId() == points3.getId()) {
//                            ifHas = true;
//                            break;
//                        }
//                    }
//                }
//                if (ifHas == true) {
//                    continue;
//                } else {
//                    pointsList1.add(points2);
//                }
//            }
//
//            //插入表-未完成
//            for (Points points : pointsList1) {
//                pointsService.addQuestionPoint(i, points.getId());
//                System.out.println("这是第" + i + "个题目的知识点");
//            }
//        }
////        for (Question question : questions) {
////            questionService.addQuestion(question);
////        }
//        return "添加题目成功";
//    }
}
