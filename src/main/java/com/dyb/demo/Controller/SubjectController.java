package com.dyb.demo.Controller;

import com.dyb.demo.Entity.Subject;
import com.dyb.demo.Mapper.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//课程管理
@Controller
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    SubjectMapper subjectMapper;

    @GetMapping("/list")
    public String findAll(Model model) {
        List<Subject> subjects = subjectMapper.findAll();
        model.addAttribute("subjects", subjects);
        return "subject";
    }

    @GetMapping("/add")
    public String addSubject() {
        return "addSubject";
    }

    @PostMapping("/add")
    public String addSubject(Subject subject) {
        subjectMapper.addSubject(subject);
        return "redirect:/subject/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") int id) {
        subjectMapper.deleteSubjectById(id);
        return "redirect:/subject/list";
    }

    @GetMapping("/update/{id}")
    public String updateById(@PathVariable("id") int id,Model model) {
        Subject subject = subjectMapper.findById(id);
        model.addAttribute("subject", subject);
        return "updateSubject";
    }

    @PostMapping("/update")
    public String updateById(Subject subject) {
        subjectMapper.updateSubjectById(subject);
        return "redirect:/subject/list";
    }
}
