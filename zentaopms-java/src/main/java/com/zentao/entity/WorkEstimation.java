package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 工作量估算实体 - 对应 zt_workestimation（与 PHP workestimation 模块一致）
 */
@Data
@Entity
@Table(name = "zt_workestimation")
public class WorkEstimation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "project", nullable = false)
    private Integer project = 0;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal scale = BigDecimal.ZERO;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal productivity = BigDecimal.ZERO;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal duration = BigDecimal.ZERO;
    @Column(name = "unitLaborCost", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitLaborCost = BigDecimal.ZERO;
    @Column(name = "totalLaborCost", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalLaborCost = BigDecimal.ZERO;

    @Column(length = 30, nullable = false)
    private String createdBy = "";
    private LocalDateTime createdDate;
    @Column(length = 30, nullable = false)
    private String editedBy = "";
    private LocalDateTime editedDate;
    @Column(length = 30, nullable = false)
    private String assignedTo = "";
    private LocalDateTime assignedDate;
    @Column(name = "dayHour", precision = 10, scale = 2, nullable = false)
    private BigDecimal dayHour = BigDecimal.ZERO;
    private Integer deleted = 0;
}
