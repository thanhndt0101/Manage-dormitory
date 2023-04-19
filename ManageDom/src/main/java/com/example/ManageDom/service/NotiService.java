package com.example.ManageDom.service;

import com.example.ManageDom.dao.NotiResponsitory;
import com.example.ManageDom.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotiService {
    @Autowired
    NotiResponsitory repo;
    public void save(Notification notification){
        repo.save(notification);
    }
    public List<Notification> listNotiBySid(Integer sid){
        return (List<Notification>) repo.findBySid(sid);
    }
}
