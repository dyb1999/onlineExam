package com.dyb.demo.Controller;

import com.dyb.demo.Entity.QuestionType;
import com.dyb.demo.Service.QuestionTypeService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

//题型管理
@Controller
@RequestMapping("/type")
public class QuestionTypeController {
    @Autowired
    QuestionTypeService questionTypeService;

    @GetMapping("/list")
    public String findAll(Model model) {
        List<QuestionType> questionTypes = questionTypeService.findAll();
        model.addAttribute("types", questionTypes);
        return "type";
    }

    @GetMapping("/add")
    public String addType() {
        return "addType";
    }

    @PostMapping("/add")
    public String addType(QuestionType questionType) {
        questionTypeService.addQuestionType(questionType);
        return "redirect:/type/list";
    }

    @GetMapping("/delete/{type}")
    public String deleteByType(@PathVariable("type") int type) {
        questionTypeService.deleteByType(type);
        return "redirect:/type/list";
    }

    @GetMapping("/update/{type}")
    public String updateByType(@PathVariable("type") int type, Model model) {
        QuestionType questionType = questionTypeService.findByType(type);
        model.addAttribute("type", questionType);
        return "updateType";
    }

    @PostMapping("/update")
    public String updateByType(QuestionType questionType) {
        questionTypeService.updateByType(questionType);
        return "redirect:/type/list";
    }
}
