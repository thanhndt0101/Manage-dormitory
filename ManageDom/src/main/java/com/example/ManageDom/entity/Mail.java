package com.example.ManageDom.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="mail")
@Data
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private LocalDateTime dateSent;
    @Column(nullable = false,unique = false,length = 100)
    private String content;
}
