package com.example.ManageDom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.ParseException;


@Service
public class CheckMail {
@Autowired
    JavaMailSender mailSender;
    public void sendEmail(String toEmail,String subject,String body) throws ParseException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vuquanghuy1996chv@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);


        mailSender.send(message);

    }
}
