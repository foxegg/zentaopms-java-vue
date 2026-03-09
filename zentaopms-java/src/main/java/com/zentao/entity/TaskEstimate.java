package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "zt_taskestimate")
public class TaskEstimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer task = 0;
    private LocalDate date;
    @Column(precision = 10, scale = 2)
    private BigDecimal left_ = BigDecimal.ZERO;
    @Column(precision = 10, scale = 2)
    private BigDecimal consumed = BigDecimal.ZERO;
    @Column(length = 30)
    private String account;
    @Column(columnDefinition = "text")
    private String work;
    @Column(name = "`order`")
    private Integer orderNum = 0;
}
