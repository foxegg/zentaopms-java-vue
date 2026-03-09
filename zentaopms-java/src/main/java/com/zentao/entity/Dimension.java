package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/** 维度 - 对应 zt_dimension */
@Data
@Entity
@Table(name = "zt_dimension")
public class Dimension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 90, nullable = false)
    private String name = "";
    @Column(length = 45, nullable = false)
    private String code = "";
    @Column(name = "`desc`", columnDefinition = "text")
    private String description;
    private Integer deleted = 0;
    private LocalDateTime createdDate;
    private LocalDateTime editedDate;
}
