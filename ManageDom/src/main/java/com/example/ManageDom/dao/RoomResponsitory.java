package com.example.ManageDom.dao;


import com.example.ManageDom.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomResponsitory extends JpaRepository<Room,String> {
    @Query("select r from Room r where concat(r.rid,r.dom,r.grade,r.type,r.current) like %?1%")
    public Page<Room> findAll(String keyword, Pageable pageable);
}
