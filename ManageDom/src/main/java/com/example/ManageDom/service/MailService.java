package com.example.ManageDom.service;

import com.example.ManageDom.dao.MailResponsitory;
import com.example.ManageDom.entity.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailService {
    @Autowired private MailResponsitory repo;
    public void save(Mail mail){
        repo.save(mail);
    }
    public List<Mail> listAllMail(){
        return (List<Mail>) repo.findAll();
    }
}
