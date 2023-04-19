package com.example.ManageDom.dao;


import com.example.ManageDom.entity.Request;
import com.example.ManageDom.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserResponsitory extends JpaRepository <User,Integer> {
    Optional<User> findByEmail(String email);
    @Query("select u from User u where u.email = :email")
    public User getUserByEmail(@Param("email") String email);
    @Query("select u from User u where u.room.rid = :rid")
    List<User> findByrid(String rid);
    @Query("select u from User u where concat(u.id,u.room.rid,u.email,u.name,u.PhoneNumber,u.address,u.status) like %?1%")
    public Page<User> findAll(String keyword, Pageable pageable);
    @Query("select u from User u where u.DateBook < :fakedate")
    List<User> findByFakeDate(LocalDate fakedate);
}
