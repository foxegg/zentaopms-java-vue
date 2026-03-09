package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 阶段实体 - 对应 zt_stage
 */
@Data
@Entity
@Table(name = "zt_stage")
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer workflowGroup = 0;
    @Column(length = 255)
    private String name;
    @Column(length = 255)
    private String percent;
    @Column(length = 255)
    private String type;
    @Column(length = 30)
    private String projectType;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String editedBy;
    private LocalDateTime editedDate;
    @Column(name = "`order`")
    private Integer orderNum = 0;
    private Integer deleted = 0;
}
