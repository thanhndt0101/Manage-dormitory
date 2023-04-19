package com.example.ManageDom.entity;
import javax.persistence.*;
import lombok.Data;

import java.util.Collection;



@Entity
@Table(name="room")
@Data
public class Room {
    @Id
    private String rid;
    @Column(nullable = false,unique = false,length = 30)
    private String type;
    @Column(nullable = false,unique = false,length = 30)
    private String dom;
    @Column(nullable = false,unique = false,length = 30)
    private String grade;
    @Column(nullable = false,unique = false,length = 30)
    private int current;


}
