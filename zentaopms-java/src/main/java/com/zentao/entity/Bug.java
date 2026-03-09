package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Bug 实体 - 对应 zt_bug
 */
@Data
@Entity
@Table(name = "zt_bug")
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private Integer product = 0;
    @Column(length = 30)
    private String injection;
    @Column(length = 30)
    private String identify;
    private Integer branch = 0;
    private Integer module = 0;
    private Integer execution = 0;
    private Integer plan = 0;
    private Integer story = 0;
    private Integer storyVersion = 1;
    private Integer task = 0;
    private Integer toTask = 0;
    private Integer toStory = 0;
    @Column(length = 255)
    private String title;
    @Column(length = 255)
    private String keywords;
    private Integer severity = 0;
    private Integer pri = 0;
    @Column(length = 30)
    private String type;
    @Column(length = 255)
    private String os;
    @Column(length = 255)
    private String browser;
    @Column(length = 30)
    private String hardware;
    @Column(length = 30)
    private String found;
    @Column(columnDefinition = "mediumtext")
    private String steps;
    @Column(length = 10)
    private String status = "active";
    @Column(length = 30)
    private String subStatus;
    @Column(length = 7)
    private String color;
    private Integer confirmed = 0;
    private Integer activatedCount = 0;
    private LocalDateTime activatedDate;
    @Column(length = 100)
    private String feedbackBy;
    @Column(length = 100)
    private String notifyEmail;
    @Column(columnDefinition = "text")
    private String mailto;
    @Column(length = 30)
    private String openedBy;
    private LocalDateTime openedDate;
    @Column(length = 255)
    private String openedBuild;
    @Column(length = 30)
    private String assignedTo;
    private LocalDateTime assignedDate;
    private LocalDate deadline;
    @Column(length = 30)
    private String resolvedBy;
    @Column(length = 30)
    private String resolution;
    @Column(length = 30)
    private String resolvedBuild;
    private LocalDateTime resolvedDate;
    @Column(length = 30)
    private String closedBy;
    private LocalDateTime closedDate;
    private Integer duplicateBug = 0;
    @Column(length = 255)
    private String relatedBug;
    @Column(name = "`case`")
    private Integer caseId = 0;
    private Integer caseVersion = 1;
    private Integer feedback = 0;
    private Integer result = 0;
    private Integer repo = 0;
    private Integer mr = 0;
    @Column(columnDefinition = "text")
    private String entry;
    @Column(length = 10)
    private String lines;
    @Column(length = 255)
    private String v1;
    @Column(length = 255)
    private String v2;
    @Column(length = 30)
    private String repoType;
    @Column(length = 50)
    private String issueKey;
    private Integer testtask = 0;
    @Column(length = 30)
    private String lastEditedBy;
    private LocalDateTime lastEditedDate;
    private Integer deleted = 0;
}
