package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/** 积分记录 - 对应 zt_score */
@Data
@Entity
@Table(name = "zt_score")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30, nullable = false)
    private String account = "";
    @Column(length = 30)
    private String module = "";
    @Column(length = 30)
    private String method = "";
    @Column(name = "`desc`", length = 250)
    private String description = "";
    private Integer beforeScore = 0;
    private Integer score = 0;
    private Integer afterScore = 0;
    private LocalDateTime time;
}
