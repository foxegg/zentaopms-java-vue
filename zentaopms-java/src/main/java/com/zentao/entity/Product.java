package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 产品实体 - 对应 zt_product
 */
@Data
@Entity
@Table(name = "zt_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer program = 0;
    @Column(length = 110)
    private String name;
    @Column(length = 45)
    private String code;
    private Integer shadow = 0;
    private Integer bind = 0;
    private Integer line = 0;
    @Column(length = 30)
    private String type = "normal";
    @Column(length = 30)
    private String status;
    @Column(length = 30)
    private String subStatus;
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    @Column(name = "`PO`", length = 30)
    private String po;
    @Column(name = "QD", length = 30)
    private String qd;
    @Column(name = "RD", length = 30)
    private String rd;
    @Column(length = 30)
    private String feedback;
    @Column(length = 30)
    private String ticket;
    private Integer workflowGroup = 0;
    @Column(length = 10)
    private String acl = "open";
    @Column(columnDefinition = "text")
    private String groups;
    @Column(columnDefinition = "text")
    private String whitelist;
    @Column(columnDefinition = "text")
    private String reviewer;
    @Column(name = "PMT", columnDefinition = "text")
    private String pmt;
    private Integer draftEpics = 0;
    private Integer activeEpics = 0;
    private Integer changingEpics = 0;
    private Integer reviewingEpics = 0;
    private Integer finishedEpics = 0;
    private Integer closedEpics = 0;
    private Integer totalEpics = 0;
    private Integer draftRequirements = 0;
    private Integer activeRequirements = 0;
    private Integer changingRequirements = 0;
    private Integer reviewingRequirements = 0;
    private Integer finishedRequirements = 0;
    private Integer closedRequirements = 0;
    private Integer totalRequirements = 0;
    private Integer draftStories = 0;
    private Integer activeStories = 0;
    private Integer changingStories = 0;
    private Integer reviewingStories = 0;
    private Integer finishedStories = 0;
    private Integer closedStories = 0;
    private Integer totalStories = 0;
    private Integer unresolvedBugs = 0;
    private Integer closedBugs = 0;
    private Integer fixedBugs = 0;
    private Integer totalBugs = 0;
    private Integer plans = 0;
    private Integer releases = 0;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String editedBy;
    private LocalDateTime editedDate;
    @Column(name = "`order`")
    private Integer orderNum = 0;
    @Column(length = 10)
    private String vision = "rnd";
    private Integer deleted = 0;
}
