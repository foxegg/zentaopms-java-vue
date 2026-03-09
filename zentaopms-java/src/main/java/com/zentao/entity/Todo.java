package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String account;
    private LocalDate date;
    @Column(length = 4)
    private String begin;
    @Column(length = 4)
    private String end;
    private Integer feedback = 0;
    @Column(length = 15)
    private String type;
    private Integer cycle = 0;
    private Integer objectID = 0;
    private Integer pri = 0;
    @Column(length = 150)
    private String name;
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    @Column(length = 10)
    private String status = "wait";
    @Column(name = "`private`")
    private Integer private_ = 0;
    @Column(length = 1000)
    private String config;
    @Column(length = 30)
    private String assignedTo;
    @Column(length = 30)
    private String assignedBy;
    private LocalDateTime assignedDate;
    @Column(length = 30)
    private String finishedBy;
    private LocalDateTime finishedDate;
    @Column(length = 30)
    private String closedBy;
    private LocalDateTime closedDate;
    @Column(length = 10)
    private String vision = "rnd";
    private Integer deleted = 0;
}
