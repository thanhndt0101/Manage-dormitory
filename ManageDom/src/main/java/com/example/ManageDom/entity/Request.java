package com.example.ManageDom.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="request")
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false,unique = false,length = 30)
    private Integer sid;
    @Column(nullable = false,unique = false,length = 30)
    private String type;
    @Column(nullable = false,unique = false,length = 30)
    private String oldRoom;
    @Column(nullable = false,unique = false,length = 30)
    private String newRoom;
    @Column(nullable = false,unique = false,length = 30)
    private String status;

}