package com.example.ManageDom.dao;

import com.example.ManageDom.entity.Notification;
import com.example.ManageDom.entity.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotiResponsitory extends JpaRepository<Notification,Integer> {
    @Query("select n from Notification n where n.user.id=?1")
    List<Notification> findBySid(Integer sid);
}
