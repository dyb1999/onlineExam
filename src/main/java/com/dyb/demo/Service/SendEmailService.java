package com.dyb.demo.Service;

import org.springframework.stereotype.Service;

@Service
public interface SendEmailService {

    //发送邮箱验证码
    void sendEmailVerCode(String to);
}
