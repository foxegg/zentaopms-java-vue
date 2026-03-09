package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_case")
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private Integer product = 0;
    private Integer execution = 0;
    private Integer branch = 0;
    private Integer lib = 0;
    private Integer module = 0;
    private Integer path = 0;
    private Integer story = 0;
    private Integer storyVersion = 1;
    @Column(length = 255)
    private String title;
    @Column(columnDefinition = "text")
    private String precondition;
    @Column(length = 255)
    private String keywords;
    private Integer pri = 3;
    @Column(length = 30)
    private String type;
    @Column(length = 10)
    private String auto = "no";
    @Column(length = 10)
    private String frame;
    @Column(length = 255)
    private String stage;
    @Column(length = 30)
    private String howRun;
    @Column(columnDefinition = "longtext")
    private String script;
    @Column(length = 30)
    private String scriptedBy;
    private LocalDateTime scriptedDate;
    @Column(length = 30)
    private String scriptStatus;
    @Column(length = 255)
    private String scriptLocation;
    @Column(length = 30)
    private String status;
    @Column(length = 30)
    private String subStatus;
    @Column(length = 7)
    private String color;
    private Integer frequency = 1;
    @Column(name = "`order`")
    private Integer orderNum = 0;
    @Column(length = 30)
    private String openedBy;
    private LocalDateTime openedDate;
    @Column(length = 255)
    private String reviewedBy;
    private LocalDate reviewedDate;
    @Column(length = 30)
    private String lastEditedBy;
    private LocalDateTime lastEditedDate;
    private Integer version = 1;
    @Column(length = 255)
    private String linkCase;
    private Integer fromBug = 0;
    private Integer fromCaseID = 0;
    private Integer fromCaseVersion = 1;
    @Column(length = 30)
    private String lastRunner;
    private LocalDateTime lastRunDate;
    @Column(length = 30)
    private String lastRunResult;
    private Integer scene = 0;
    private Integer sort = 0;
    private Integer deleted = 0;
}
