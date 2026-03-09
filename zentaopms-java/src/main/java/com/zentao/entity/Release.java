package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_release")
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255)
    private String project = "0";
    private Integer product = 0;
    @Column(length = 255)
    private String branch = "0";
    private Integer shadow = 0;
    @Column(length = 255)
    private String build;
    @Column(length = 255)
    private String name;
    private Integer system = 0;
    @Column(length = 255)
    private String releases;
    private Integer marker = 0;
    private LocalDate date;
    private LocalDate releasedDate;
    @Column(columnDefinition = "text")
    private String stories;
    @Column(columnDefinition = "text")
    private String bugs;
    @Column(columnDefinition = "text")
    private String leftBugs;
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    @Column(columnDefinition = "text")
    private String mailto;
    @Column(length = 255)
    private String notify;
    @Column(length = 20)
    private String status = "normal";
    @Column(length = 30)
    private String subStatus;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    private Integer deleted = 0;
}
