package com.dyb.demo.Controller;

import com.dyb.demo.Entity.PaperScoreRecord;
import com.dyb.demo.Entity.Score;
import com.dyb.demo.Mapper.PaperMapper;
import com.dyb.demo.Mapper.PaperScoreRecordMapper;
import com.dyb.demo.Mapper.ScoreMapper;
import com.dyb.demo.Service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/totalscore")
public class ScoreController {

    @Autowired
    ScoreService scoreService;

    //所有学生 所有科目 得分
    @GetMapping("/list")
    public String findAll(Model model) {
        List<Score> scores = scoreService.findAll();
        model.addAttribute("scores", scores);
        return "scoreView";
    }

    //分科目查看所有学生
    @PostMapping("/list/exam")
    public String findByExamcode(@RequestParam("examcode") int examcode, Model model) {
        List<Score> scores = scoreService.findAllRecordByExamcode(examcode);
        model.addAttribute("scores", scores);
        return "scoreView";
    }

    //分学号查看某个学生的各科目成绩
    @PostMapping("/list/stu")
    public String findByLoginNumber(@RequestParam("loginNumber") String loginNumber, Model model) {
        List<Score> scores = scoreService.findAllRecordByNumber(loginNumber);
        model.addAttribute("scores", scores);
        return "scoreView";
    }

    //TODO-计算排名并更新等级
    @PostMapping("/calculateRange")
    public String calculateRange(@RequestParam("examcode") int examcode, Model model) {
        List<Score> scores = scoreService.findAllRecordByExamcode(examcode);
        scoreService.sortOfScore(examcode);
        model.addAttribute("scores", scores);
        return "examSearchResult";
    }

    //删除某个学生的某科考试的总成绩记录和所有题目记录
    @PostMapping("/delete1")
    public String deleteByNumberAndSubject(@RequestParam("loginNumber") String loginNumber, @RequestParam("examcode") int examcode) {
        scoreService.deleteByNumberAndSubject(loginNumber, examcode);
        return "redirect:/totalscore/list";
    }

    //TODO-分考试编号即科目查看各个学生成绩，ps:可图表化
    @PostMapping("/list/charts")
    public String showCharts(@RequestParam("examcode") int examcode, Model model) {
        List<Score> scores = scoreService.findAllRecordByExamcode(examcode);
        int[] nums = new int[]{0,0,0,0};
        for (Score score : scores) {
            if (score.getLevel().equalsIgnoreCase("A")) {
                nums[0]++;
            }
            if (score.getLevel().equalsIgnoreCase("B")) {
                nums[1]++;
            }
            if (score.getLevel().equalsIgnoreCase("C")) {
                nums[2]++;
            }
            if (score.getLevel().equalsIgnoreCase("D")) {
                nums[3]++;
            }
        }
        model.addAttribute("nums", nums);
        return "scoreCharts1";
    }

    //计算某个学生的总分，设计为不能更改，确保总分为所有题目得分之和
    @PostMapping("/calculateSomeone")
    public String calculate1(int examcode, int paperid, String loginNumber) {
        scoreService.updateScore(examcode, paperid, loginNumber);
        return "redirect:/totalscore/list";
    }

    //计算某个考试的所有学生的得分
    @PostMapping("calculatePart")
    public String calculate2(int examcode, int paperid) {
        List<String> numbers = scoreService.findAllLoginNumber(examcode, paperid);
        for (String number : numbers) {
            scoreService.updateScore(examcode, paperid, number);
        }
        return "redirect:/totalscore/list";
    }
}
