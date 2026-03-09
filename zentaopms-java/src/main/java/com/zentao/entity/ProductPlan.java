package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_productplan")
public class ProductPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer product = 0;
    @Column(length = 255)
    private String branch = "0";
    private Integer parent = 0;
    @Column(length = 90)
    private String title;
    @Column(length = 10)
    private String status = "wait";
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    private LocalDate begin;
    private LocalDate end;
    private LocalDateTime finishedDate;
    private LocalDateTime closedDate;
    @Column(name = "`order`", columnDefinition = "text")
    private String order_;
    @Column(length = 20)
    private String closedReason;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    private Integer deleted = 0;
}
