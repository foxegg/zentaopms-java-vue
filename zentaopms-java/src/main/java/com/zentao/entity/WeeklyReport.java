package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 周报实体 - 对应 zt_weeklyreport
 */
@Data
@Entity
@Table(name = "zt_weeklyreport")
public class WeeklyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private LocalDate weekStart;
    @Column(precision = 12, scale = 2)
    private BigDecimal pv = BigDecimal.ZERO;
    @Column(precision = 12, scale = 2)
    private BigDecimal ev = BigDecimal.ZERO;
    @Column(precision = 12, scale = 2)
    private BigDecimal ac = BigDecimal.ZERO;
    @Column(precision = 12, scale = 2)
    private BigDecimal sv = BigDecimal.ZERO;
    @Column(precision = 12, scale = 2)
    private BigDecimal cv = BigDecimal.ZERO;
    private Integer staff = 0;
    @Column(length = 255)
    private String progress;
    @Column(length = 255)
    private String workload;
}
