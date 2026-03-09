package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目实体 - 对应 zt_project
 */
@Data
@Entity
@Table(name = "zt_project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private Integer isTpl = 0;
    private Integer charter = 0;
    @Column(length = 30)
    private String model;
    @Column(length = 30)
    private String type = "sprint";
    @Column(length = 30)
    private String category;
    @Column(length = 30)
    private String lifetime;
    @Column(precision = 12, scale = 2)
    private BigDecimal budget = BigDecimal.ZERO;
    @Column(length = 30)
    private String budgetUnit = "CNY";
    @Column(length = 30)
    private String attribute;
    @Column(precision = 5, scale = 2)
    private BigDecimal percent = BigDecimal.ZERO;
    private Integer milestone = 0;
    @Column(columnDefinition = "text")
    private String output;
    @Column(length = 30)
    private String auth;
    @Column(length = 30)
    private String storyType = "story";
    private Integer parent = 0;
    @Column(length = 255)
    private String path;
    private Integer grade = 0;
    @Column(length = 90)
    private String name;
    @Column(length = 45)
    private String code;
    private Integer hasProduct = 1;
    private Integer workflowGroup = 0;
    private LocalDate begin;
    private LocalDate end;
    private LocalDate firstEnd;
    private LocalDate realBegan;
    private LocalDate realEnd;
    private Integer days = 0;
    @Column(length = 10)
    private String status;
    @Column(length = 30)
    private String subStatus;
    private Integer pri = 1;
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    private Integer version = 1;
    private Integer parentVersion = 1;
    private Integer planDuration = 0;
    private Integer realDuration = 0;
    @Column(precision = 5, scale = 2)
    private BigDecimal progress = BigDecimal.ZERO;
    @Column(precision = 12, scale = 2)
    private BigDecimal estimate = BigDecimal.ZERO;
    @Column(name = "`left`", precision = 12, scale = 2)
    private BigDecimal left_ = BigDecimal.ZERO;
    @Column(precision = 12, scale = 2)
    private BigDecimal consumed = BigDecimal.ZERO;
    private Integer teamCount = 0;
    private Integer market = 0;
    @Column(length = 30)
    private String openedBy;
    private LocalDateTime openedDate;
    @Column(length = 20)
    private String openedVersion;
    @Column(length = 30)
    private String lastEditedBy;
    private LocalDateTime lastEditedDate;
    @Column(length = 30)
    private String closedBy;
    private LocalDateTime closedDate;
    private Integer deleted = 0;
}
