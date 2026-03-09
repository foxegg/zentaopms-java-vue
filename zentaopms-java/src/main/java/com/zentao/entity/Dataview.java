package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/** 数据视图 - 对应 zt_dataview */
@Data
@Entity
@Table(name = "zt_dataview")
public class Dataview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "`group`")
    private Integer groupId = 0;
    @Column(length = 155, nullable = false)
    private String name = "";
    @Column(length = 50)
    private String code = "";
    @Column(length = 50)
    private String mode = "builder";
    @Column(length = 10)
    private String driver = "mysql";
    @Column(length = 57)
    private String view = "";
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String editedBy;
    private LocalDateTime editedDate;
    private Integer deleted = 0;
}
