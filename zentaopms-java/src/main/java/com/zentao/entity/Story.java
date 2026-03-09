package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 需求/用户故事实体 - 对应 zt_story
 */
@Data
@Entity
@Table(name = "zt_story")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer parent = 0;
    private Integer isParent = 0;
    private Integer root = 0;
    @Column(columnDefinition = "text")
    private String path;
    private Integer grade = 0;
    private Integer product = 0;
    private Integer branch = 0;
    private Integer module = 0;
    @Column(columnDefinition = "text")
    private String plan;
    @Column(length = 20)
    private String source;
    @Column(length = 255)
    private String sourceNote;
    private Integer fromBug = 0;
    private Integer feedback = 0;
    @Column(length = 255)
    private String title;
    @Column(length = 255)
    private String keywords;
    @Column(length = 20)
    private String type = "story";
    @Column(length = 30)
    private String category = "feature";
    private Integer pri = 3;
    @Column(precision = 10, scale = 2)
    private BigDecimal estimate = BigDecimal.ZERO;
    @Column(length = 10)
    private String status;
    @Column(length = 30)
    private String subStatus;
    @Column(length = 7)
    private String color;
    @Column(length = 10)
    private String stage = "wait";
    @Column(length = 30)
    private String stagedBy;
    @Column(columnDefinition = "text")
    private String mailto;
    private Integer lib = 0;
    private Integer fromStory = 0;
    private Integer fromVersion = 1;
    @Column(length = 30)
    private String openedBy;
    private LocalDateTime openedDate;
    @Column(length = 30)
    private String assignedTo;
    private LocalDateTime assignedDate;
    private LocalDateTime approvedDate;
    @Column(length = 30)
    private String lastEditedBy;
    private LocalDateTime lastEditedDate;
    @Column(length = 30)
    private String changedBy;
    private LocalDateTime changedDate;
    @Column(length = 255)
    private String reviewedBy;
    private LocalDateTime reviewedDate;
    private LocalDateTime releasedDate;
    @Column(length = 30)
    private String closedBy;
    private LocalDateTime closedDate;
    @Column(length = 30)
    private String closedReason;
    private LocalDateTime activatedDate;
    private Integer toBug = 0;
    @Column(length = 255)
    private String linkStories;
    @Column(length = 255)
    private String linkRequirements;
    @Column(columnDefinition = "text")
    private String docs;
    @Column(length = 255)
    private String twins;
    private Integer duplicateStory = 0;
    private Integer version = 1;
    private Integer parentVersion = 1;
    private Integer demandVersion = 1;
    private Integer storyChanged = 0;
    private Integer deleted = 0;
}
