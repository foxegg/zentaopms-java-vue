package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 任务实体 - 对应 zt_task
 */
@Data
@Entity
@Table(name = "zt_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private Integer parent = 0;
    private Integer isParent = 0;
    private Integer isTpl = 0;
    @Column(length = 255)
    private String path;
    private Integer execution = 0;
    private Integer module = 0;
    private Integer design = 0;
    private Integer story = 0;
    private Integer storyVersion = 1;
    private Integer designVersion = 1;
    private Integer fromBug = 0;
    private Integer feedback = 0;
    private Integer fromIssue = 0;
    @Column(columnDefinition = "text")
    private String docs;
    @Column(columnDefinition = "text")
    private String docVersions;
    @Column(length = 255)
    private String name;
    @Column(length = 20)
    private String type;
    @Column(length = 10)
    private String mode;
    private Integer pri = 0;
    @Column(precision = 10, scale = 2)
    private BigDecimal estimate = BigDecimal.ZERO;
    @Column(precision = 10, scale = 2)
    private BigDecimal consumed = BigDecimal.ZERO;
    @Column(name = "`left`", precision = 10, scale = 2)
    private BigDecimal left_ = BigDecimal.ZERO;
    private LocalDate deadline;
    @Column(length = 10)
    private String status = "wait";
    @Column(length = 30)
    private String subStatus;
    @Column(length = 7)
    private String color;
    @Column(columnDefinition = "text")
    private String mailto;
    @Column(length = 255)
    private String keywords;
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    private Integer version = 1;
    @Column(length = 30)
    private String openedBy;
    private LocalDateTime openedDate;
    @Column(length = 30)
    private String assignedTo;
    private LocalDateTime assignedDate;
    private LocalDate estStarted;
    private LocalDateTime realStarted;
    @Column(length = 30)
    private String finishedBy;
    private LocalDateTime finishedDate;
    @Column(columnDefinition = "text")
    private String finishedList;
    @Column(length = 30)
    private String canceledBy;
    private LocalDateTime canceledDate;
    @Column(length = 30)
    private String closedBy;
    private LocalDateTime closedDate;
    private Integer planDuration = 0;
    private Integer realDuration = 0;
    @Column(length = 30)
    private String closedReason;
    @Column(length = 30)
    private String lastEditedBy;
    private LocalDateTime lastEditedDate;
    private LocalDateTime activatedDate;
    @Column(name = "`order`")
    private Integer orderNum = 0;
    private Integer repo = 0;
    private Integer mr = 0;
    @Column(length = 255)
    private String entry;
    private Integer deleted = 0;
}
