package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_testreport")
public class TestReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private Integer product = 0;
    private Integer execution = 0;
    @Column(length = 255)
    private String tasks;
    @Column(length = 255)
    private String builds;
    @Column(length = 255)
    private String title;
    private LocalDate begin;
    private LocalDate end;
    @Column(length = 30)
    private String owner;
    @Column(columnDefinition = "text")
    private String members;
    @Column(columnDefinition = "text")
    private String stories;
    @Column(columnDefinition = "text")
    private String bugs;
    @Column(columnDefinition = "text")
    private String cases;
    @Column(columnDefinition = "text")
    private String report;
    @Column(length = 10)
    private String objectType;
    private Integer objectID = 0;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    private Integer deleted = 0;
}
