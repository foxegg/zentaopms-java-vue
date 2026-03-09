package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/** 图表 - 对应 zt_chart */
@Data
@Entity
@Table(name = "zt_chart")
public class Chart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 255, nullable = false)
    private String name = "";
    @Column(length = 255)
    private String code = "";
    @Column(length = 30)
    private String type = "";
    @Column(name = "`desc`", columnDefinition = "text")
    private String description;
    @Column(length = 10)
    private String stage = "draft";
    private Integer deleted = 0;
    private LocalDateTime createdDate;
    private LocalDateTime editedDate;
}
