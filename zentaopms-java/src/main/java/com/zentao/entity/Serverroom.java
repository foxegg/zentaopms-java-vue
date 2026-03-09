package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/** 机房 - 对应 zt_serverroom */
@Data
@Entity
@Table(name = "zt_serverroom")
public class Serverroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128, nullable = false)
    private String name = "";
    @Column(length = 128)
    private String city = "";
    @Column(length = 20)
    private String line = "";
    @Column(length = 128)
    private String bandwidth = "";
    @Column(length = 128)
    private String provider = "";
    @Column(length = 30)
    private String owner = "";
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String editedBy;
    private LocalDateTime editedDate;
    private Integer deleted = 0;
}
