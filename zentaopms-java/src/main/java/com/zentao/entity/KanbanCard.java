package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 看板卡片实体 - 对应 zt_kanbancard
 */
@Data
@Entity
@Table(name = "zt_kanbancard")
public class KanbanCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer kanban = 0;
    private Integer region = 0;
    private Integer group = 0;
    private Integer fromID = 0;
    @Column(length = 30)
    private String fromType;
    @Column(length = 255)
    private String name;
    @Column(length = 30)
    private String status = "doing";
    private Integer pri = 0;
    @Column(columnDefinition = "text")
    private String assignedTo;
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    private LocalDate begin;
    private LocalDate end;
    @Column(precision = 10, scale = 2)
    private BigDecimal estimate = BigDecimal.ZERO;
    @Column(precision = 5, scale = 2)
    private BigDecimal progress = BigDecimal.ZERO;
    @Column(length = 7)
    private String color;
    @Column(length = 10)
    private String acl = "open";
    @Column(columnDefinition = "text")
    private String whitelist;
    @Column(name = "`order`")
    private Integer orderNum = 0;
    private Integer archived = 0;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String lastEditedBy;
    private LocalDateTime lastEditedDate;
    @Column(length = 30)
    private String archivedBy;
    private LocalDateTime archivedDate;
    @Column(length = 30)
    private String assignedBy;
    private LocalDateTime assignedDate;
    private Integer deleted = 0;
}
