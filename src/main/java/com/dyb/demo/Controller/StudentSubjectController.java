package com.dyb.demo.Controller;

import com.dyb.demo.Entity.Student_Subject;
import com.dyb.demo.Mapper.Student_SubjectMapper;
import com.dyb.demo.Mapper.SubjectMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/stu")
public class StudentSubjectController {
    @Autowired
    Student_SubjectMapper student_subjectMapper;

    @Autowired
    SubjectMapper subjectMapper;

    //@RequestParam("loginNumber") String loginNumber,
    @GetMapping("/list")
    public String findAll(Model model) {
        String loginNumber = SecurityUtils.getSubject().getPrincipal().toString();//获取当前登录用户账号
        List<Student_Subject> student_subjects = student_subjectMapper.findByNumber(loginNumber);
        for (Student_Subject student_subject : student_subjects) {
            student_subject.setCoursename(subjectMapper.findById(student_subject.getSubject_id()).getCoursename());
        }
        model.addAttribute("student_subjects", student_subjects);
        return "Student_Subjects";
    }

    //删除课程
    @GetMapping("/delete/{id}")
    public String deleteRecord(@PathVariable("id") int id) {
        String loginNumber = SecurityUtils.getSubject().getPrincipal().toString();
        int subject_id = student_subjectMapper.findById(id).getSubject_id();
        student_subjectMapper.deleteByNumberSubject(loginNumber, subject_id);
        return "redirect:/stu/list";
    }

    //添加，定向到所有课程列表去
    @GetMapping("/add")
    public String addNewRecord() {
        return "redirect:/subject/list";
    }

    @RequestMapping("/add/{subject_id}")
    public String addNewRecord(@PathVariable("subject_id") int subject_id) {
        String loginNumber = SecurityUtils.getSubject().getPrincipal().toString();
        Student_Subject student_subject = new Student_Subject(loginNumber, subject_id);
        List<Student_Subject> student_subjects = student_subjectMapper.findByNumber(loginNumber);
        //不允许重复
        for (Student_Subject student_subject1 : student_subjects) {
            if (student_subject.getSubject_id() == student_subject1.getSubject_id()) {
                return "redirect:/subject/list";
            }
        }
        student_subjectMapper.addStudentSubject(student_subject);
        return "redirect:/stu/list";
    }

    //
}
