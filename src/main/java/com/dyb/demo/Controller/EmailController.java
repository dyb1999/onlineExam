package com.dyb.demo.Controller;

import com.dyb.demo.Entity.Email;
import com.dyb.demo.Service.EmailService;
import com.dyb.demo.Service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/email")
public class EmailController {
    @Autowired
    SendService sendService;

    @Autowired
    EmailService emailService;

    @Value("${spring.mail.username}")
    String from;

    @PostMapping("email")
    @ResponseBody
    public String sendEmailVerify(@RequestParam("EmailAddress") String to) {
        try {
            sendService.SendEmailVercode(to);
            return "发送成功！";
        } catch (Exception e) {
//            System.out.println("发送失败");
            e.printStackTrace();
            return "发送失败！";
        }
    }
}
