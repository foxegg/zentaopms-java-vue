package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 团队成员实体 - 对应 zt_team（项目/执行成员）
 */
@Data
@Entity
@Table(name = "zt_team", uniqueConstraints = @UniqueConstraint(columnNames = {"root", "type", "account"}))
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer root = 0;
    @Column(length = 10, nullable = false)
    private String type = "execution";
    @Column(length = 30, nullable = false)
    private String account = "";
    @Column(length = 30)
    private String role = "";
    @Column(length = 30)
    private String position = "";
    @Column(length = 8)
    private String limited = "no";
    @Column(name = "`join`")
    private LocalDate joinDate;
    private Integer days = 0;
    @Column(precision = 3, scale = 1)
    private BigDecimal hours = BigDecimal.ZERO;
    @Column(precision = 10, scale = 2)
    private BigDecimal estimate = BigDecimal.ZERO;
    @Column(precision = 10, scale = 2)
    private BigDecimal consumed = BigDecimal.ZERO;
    @Column(name = "`left`", precision = 10, scale = 2)
    private BigDecimal leftHours = BigDecimal.ZERO;
    @Column(name = "`order`")
    private Integer orderNum = 0;
}
