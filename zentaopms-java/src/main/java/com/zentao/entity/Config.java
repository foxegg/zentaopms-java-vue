package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 系统配置实体 - 对应 zt_config
 */
@Data
@Entity
@Table(name = "zt_config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 10)
    private String vision;
    @Column(length = 30)
    private String owner;
    @Column(length = 30)
    private String module;
    @Column(length = 30)
    private String section;
    @Column(name = "`key`", length = 30)
    private String key_;
    @Column(columnDefinition = "longtext")
    private String value;
}
