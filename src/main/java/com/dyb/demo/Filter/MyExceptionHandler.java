package com.dyb.demo.Filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler {
    @ExceptionHandler()
    public String ErrorHandler0(Exception e) {
        log.error("其它错误", e);
        return "unknownError";
    }

    @ExceptionHandler
    public String ErrorHandler1(AuthenticationException e) {
        log.error("用户名/密码错误", e);
        return "redirect:/user/AuthenticationException";
    }

    @ExceptionHandler
    public String ErrorHandler2(AuthorizationException e) {
        log.error("未通过权限认证", e);
        return "redirect:/user/AuthorizationException";
    }


    @ExceptionHandler
    public String ErrorHandler3(UnknownAccountException e) {
        log.error("用户不存在", e);
        return "redirect:/user/UnknownAccountException";
    }


//    @ExceptionHandler
//    public String ErrorHandler(Exception e) {
//        if (e)
//    }



}
