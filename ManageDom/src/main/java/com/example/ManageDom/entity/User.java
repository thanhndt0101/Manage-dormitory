package com.example.ManageDom.entity;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name= "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false,length = 30)
    private String name;
    @Column(nullable = false,unique = true,length = 30)
    private String email;
    @Column(nullable = false,length = 60)
    private String password;
    @Column(nullable = false,length = 30)
    private boolean enabled;
    @Column(nullable = false,length = 30)
    private double coin;
    @Column(nullable = false,length = 30)
    private String address;
    @Column(nullable = false,length = 30)
    private Integer PhoneNumber;
    @Column
    private LocalDate DateBook;
    @Column
    private String status;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles" ,
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "rid")
    private Room room;
    @Column(nullable = true,length = 64)
    private String photos;

}