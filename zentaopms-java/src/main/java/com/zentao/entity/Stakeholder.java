package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 干系人实体 - 对应 zt_stakeholder
 */
@Data
@Entity
@Table(name = "zt_stakeholder")
public class Stakeholder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer objectID = 0;
    @Column(length = 10)
    private String objectType;
    @Column(length = 30)
    private String user;
    @Column(length = 30)
    private String type;
    private Integer key = 0;
    @Column(name = "`from`", length = 30)
    private String fromType;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String editedBy;
    private LocalDateTime editedDate;
    private Integer deleted = 0;
}
