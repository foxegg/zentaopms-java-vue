package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 看板实体 - 对应 zt_kanban
 */
@Data
@Entity
@Table(name = "zt_kanban")
public class Kanban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer space = 0;
    @Column(length = 255)
    private String name;
    @Column(length = 30)
    private String owner;
    @Column(columnDefinition = "text")
    private String team;
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    @Column(length = 10)
    private String acl = "open";
    @Column(columnDefinition = "text")
    private String whitelist;
    private Integer archived = 1;
    private Integer performable = 0;
    @Column(length = 10)
    private String status = "active";
    @Column(name = "`order`")
    private Integer orderNum = 0;
    private Integer displayCards = 0;
    private Integer showWIP = 1;
    private Integer fluidBoard = 0;
    private Integer colWidth = 264;
    private Integer minColWidth = 200;
    private Integer maxColWidth = 384;
    @Column(length = 255)
    private String object;
    @Column(length = 10)
    private String alignment = "center";
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String lastEditedBy;
    private LocalDateTime lastEditedDate;
    @Column(length = 30)
    private String closedBy;
    private LocalDateTime closedDate;
    @Column(length = 30)
    private String activatedBy;
    private LocalDateTime activatedDate;
    private Integer deleted = 0;
}
