package com.example.ManageDom.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name= "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "sid")
    private User user;

    private String message;
}
