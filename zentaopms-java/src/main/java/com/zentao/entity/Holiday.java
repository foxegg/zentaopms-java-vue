package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * 节假日 - 对应 zt_holiday，与 PHP module/holiday 一致。
 */
@Data
@Entity
@Table(name = "zt_holiday")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false)
    private String name = "";

    @Column(length = 10, nullable = false)
    private String type = "holiday";

    @Column(name = "`desc`", columnDefinition = "text")
    private String description;

    @Column(name = "`year`", length = 4, nullable = false)
    private String year = "";

    private LocalDate begin;
    private LocalDate end;
}
