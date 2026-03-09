package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 构建记录实体 - 对应 zt_compile
 */
@Data
@Entity
@Table(name = "zt_compile")
public class Compile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;
    private Integer job = 0;
    private Integer queue = 0;
    @Column(length = 100)
    private String status;
    @Column(length = 255)
    private String branch;
    @Column(columnDefinition = "longtext")
    private String logs;
    @Column(length = 10)
    private String atTime;
    private Integer testtask = 0;
    @Column(length = 255)
    private String tag;
    private Integer times = 0;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private Integer deleted = 0;
}
