package com.dyb.demo.Controller;

import com.dyb.demo.Entity.User;
import com.dyb.demo.Mapper.UserMapper;
import com.dyb.demo.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

//用户管理
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/logout")
    public String logOut() {
        return "login";
    }

    @GetMapping("/index")
    public String getIndex() {
        //首页
        return "index";
    }


    @GetMapping("/AuthorizationException")
    public String AuthorizationError() {
        return "AuthorizationException";
    }

    @GetMapping("/AuthenticationException")
    public String AuthenticationError() {
        return "AuthenticationException";
    }

    @GetMapping("/UnknownAccountException")
    public String UnknownAccountError() {
        return "UnknownAccountException";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //TODO-登录权限控制
    @PostMapping("/login")
    public String login(@RequestParam("loginNumber") String loginNumber, @RequestParam("password") String password, Model model) {
        if (StringUtils.isEmpty(loginNumber) || StringUtils.isEmpty(password)) {
            return "请输入用户名和密码！";
        }
        //用户认证
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginNumber, password);

        try {
            //进行验证
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException e) {
            log.error("用户名不存在！", e);
            System.out.println("用户名不存在");
            return "UnknownAccountException";
        } catch (IncorrectCredentialsException e) {
            log.error("账号或密码错误！", e);
            System.out.println("账号或密码错误");
            return "AuthenticationException";
        } catch (AuthorizationException e) {
            log.error("没有权限！", e);
            System.out.println("没有权限");
            return "AuthorizationException";
        }
        User user = userService.getUserByNumber(String.valueOf(subject.getPrincipal()));
        Integer role = user.getRole();
        String rolename;
        if (role == 0) {
            rolename = "管理员";
        } else if (role == 1) {
            rolename = "老师";
        } else {
            rolename = "学生";
        }
        model.addAttribute("rolename", rolename);
        model.addAttribute("user", user);
        return "index";
    }

    // 注册界面
   @GetMapping("/register")
    public String Register() {
        return "register";
    }


    @RequiresRoles("0")
    @GetMapping("/list")
    public String getAllUser(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    //返回搜索结果
    @PostMapping("/select")
    public String selectByName(@RequestParam("var") String var, Model model) {
        List<User> users = userService.selectByName(var);
        if (users.size() == 0) {
            users = userService.selectByNumer(var);
        }
        model.addAttribute("users", users);
        return "users1";
    }

    //添加用户,用户注册界面
    @GetMapping("/add")
    public String addUser() {
        return "register";
    }

    //添加用户操作
    @PostMapping("/add")
    public String addUser(User user) {
        String LoginNumber = user.getLoginNumber();
        User user1 = userService.getUserByNumber(LoginNumber);
        if (user1 == null) {
            userService.addUser(user);
            System.out.println("添加成功！");
            return "redirect:/user/list";
        } else {
            return "numberFail";
        }
    }

    //返回更新页面
    @GetMapping("/update/{number}")
    public String updateUserByNumber(@PathVariable("number") String number, Model model) {
        User user = userService.getUserByNumber(number);
        model.addAttribute("user", user);
        return "update";
    }

    //更新用户信息
    @PostMapping("/update")
    public String updateUserByNumber(User user) {
        userService.updateUserByNumber(user);
        return "redirect:/user/list";
    }

    //删除用户-学号
    @GetMapping("/delete/{number}")
    public String deleteByNumber(@PathVariable("number") String number) {
        userService.deleteByNumber(number);
        System.out.println("删除成功！");
        return "redirect:/user/list";
    }
}
