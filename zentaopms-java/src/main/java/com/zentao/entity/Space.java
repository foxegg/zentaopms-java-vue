package com.zentao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 空间实体 - 对应 zt_space（与 PHP space 模块一致）
 */
@Data
@Entity
@Table(name = "zt_space")
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200, nullable = false)
    private String name = "";
    @Column(length = 64, nullable = false)
    private String k8space = "";
    @Column(length = 30, nullable = false)
    private String owner = "";
    @Column(name = "`default`")
    @JsonProperty("default")
    private Integer default_ = 0;
    private LocalDateTime createdAt;
    private Integer deleted = 0;
}
