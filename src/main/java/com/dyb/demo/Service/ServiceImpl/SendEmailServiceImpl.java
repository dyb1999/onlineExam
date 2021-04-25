package com.dyb.demo.Service.ServiceImpl;

import com.dyb.demo.Service.SendEmailService;
import org.springframework.stereotype.Service;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    //发邮件
    @Override
    public void sendEmailVerCode(String to) {
    }
}
