package com.example.ManageDom.entity;

import javax.persistence.*;
import lombok.Data;

import java.util.Collection;

@Entity
@Table(name="roles")
@Data
public class Role {
    @Id
    @Column(name="role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;
}
