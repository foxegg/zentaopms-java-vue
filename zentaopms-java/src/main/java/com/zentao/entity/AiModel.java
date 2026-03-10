package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/** 对应 zt_ai_model */
@Data
@Entity
@Table(name = "zt_ai_model")
public class AiModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String type = "";
    @Column(length = 20, nullable = false)
    private String vendor = "";
    @Column(columnDefinition = "text")
    private String credentials;
    @Column(columnDefinition = "text")
    private String proxy;
    @Column(length = 20, nullable = false)
    private String name = "";
    @Column(name = "`desc`", columnDefinition = "text")
    private String description;
    @Column(length = 30, nullable = false)
    private String createdBy = "";
    private LocalDateTime createdDate;
    @Column(length = 30, nullable = false)
    private String editedBy = "";
    private LocalDateTime editedDate;
    private Integer enabled = 1;
    private Integer deleted = 0;
}
