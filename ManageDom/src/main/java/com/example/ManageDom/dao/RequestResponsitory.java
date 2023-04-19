package com.example.ManageDom.dao;


import com.example.ManageDom.entity.Request;
import com.example.ManageDom.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestResponsitory extends JpaRepository<Request,Integer> {
    List<Request> findBysid(Integer sid);
    @Query("select r from Request r where concat(r.id,r.newRoom,r.oldRoom,r.type,r.status) like %?2% and r.sid =?1")
    public Page<Request> findBysid(Integer sid,String keyword, Pageable pageable);
    @Query("select r from Request r where r.sid =?1")
    public Page<Request> findBysid(Integer sid, Pageable pageable);
    @Query("select r from Request r where concat(r.id,r.newRoom,r.oldRoom,r.type,r.status) like %?1%")
    public Page<Request> findAll(String keyword, Pageable pageable);
}
