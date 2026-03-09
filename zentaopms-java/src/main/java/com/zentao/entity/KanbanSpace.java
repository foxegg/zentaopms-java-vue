package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 看板空间实体 - 对应 zt_kanbanspace
 */
@Data
@Entity
@Table(name = "zt_kanbanspace")
public class KanbanSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255)
    private String name;
    @Column(length = 50)
    private String type;
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
    @Column(length = 10)
    private String status = "active";
    @Column(name = "`order`")
    private Integer orderNum = 0;
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
