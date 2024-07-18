package com.sky.common.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class MailUtil {
    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String MAIL_FROM;


    public void send(String MAIL_TO, String MAIL_SUBJECT, String MAIL_TEXT) {
        SimpleMailMessage message = new SimpleMailMessage();
        // 发件箱
        message.setFrom(MAIL_FROM);
        // 收件箱可以是多个，用 String[] 表示多个收件箱
        message.setTo(MAIL_TO);
        // 邮件主题
        message.setSubject(MAIL_SUBJECT);
        // 邮件正文
        message.setText(MAIL_TEXT);
        javaMailSender.send(message);
    }
}
