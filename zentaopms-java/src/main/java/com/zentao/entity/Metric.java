package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 统计指标实体 - 对应 zt_metric
 */
@Data
@Entity
@Table(name = "zt_metric")
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String purpose = "";
    @Column(length = 30)
    private String scope = "";
    @Column(length = 30)
    private String object = "";
    @Column(length = 10)
    private String stage = "wait";
    @Column(length = 10)
    private String type = "php";
    @Column(length = 90)
    private String name = "";
    @Column(length = 90)
    private String alias = "";
    @Column(length = 90)
    private String code = "";
    @Column(length = 10)
    private String unit = "";
    @Column(length = 50)
    private String dateType = "";
    @Column(name = "`desc`", columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String definition;
    @Column(name = "`when`", length = 30)
    private String whenCol = "";
    @Column(length = 30)
    private String createdBy = "";
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String editedBy = "";
    private LocalDateTime editedDate;
    private Integer builtin = 0;
    @Column(name = "`order`")
    private Integer orderNum = 0;
    private Integer deleted = 0;
}
