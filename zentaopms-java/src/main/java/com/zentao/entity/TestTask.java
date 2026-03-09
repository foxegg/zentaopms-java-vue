package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_testtask")
public class TestTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private Integer product = 0;
    @Column(length = 90)
    private String name;
    private Integer execution = 0;
    private Integer build = 0;
    private Integer joint = 0;
    @Column(length = 255)
    private String type;
    @Column(length = 30)
    private String owner;
    private Integer pri = 0;
    private LocalDate begin;
    private LocalDate end;
    private LocalDate realBegan;
    private LocalDateTime realFinishedDate;
    @Column(columnDefinition = "text")
    private String mailto;
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    @Column(columnDefinition = "text")
    private String report;
    @Column(length = 10)
    private String status = "wait";
    private Integer testreport = 0;
    @Column(length = 10)
    private String auto = "no";
    @Column(length = 30)
    private String subStatus;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(columnDefinition = "text")
    private String members;
    private Integer deleted = 0;
}
