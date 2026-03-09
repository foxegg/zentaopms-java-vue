package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/** 大屏 - 对应 zt_screen */
@Data
@Entity
@Table(name = "zt_screen")
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer dimension = 0;
    @Column(length = 255, nullable = false)
    private String name = "";
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    @Column(length = 10)
    private String status = "draft";
    private Integer deleted = 0;
    private LocalDateTime createdDate;
    private LocalDateTime editedDate;
}
