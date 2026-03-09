package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/** 流水线 - 对应 zt_pipeline */
@Data
@Entity
@Table(name = "zt_pipeline")
public class Pipeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 30)
    private String type = "";
    @Column(length = 50)
    private String name = "";
    @Column(length = 255)
    private String url = "";
    @Column(length = 30)
    private String account = "";
    @Column(length = 255)
    private String password = "";
    @Column(length = 255)
    private String token = "";
    @Column(name = "`private`", length = 32)
    private String privateKey = "";
    private Integer instanceID = 0;
    private Integer deleted = 0;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String editedBy;
    private LocalDateTime editedDate;
}
