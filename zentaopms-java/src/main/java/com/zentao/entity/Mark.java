package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标记实体 - 对应 zt_mark（与 PHP mark 模块一致，用于收藏/标星等）
 */
@Data
@Entity
@Table(name = "zt_mark")
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 10, nullable = false)
    private String objectType = "";
    private Integer objectID = 0;
    @Column(length = 10, nullable = false)
    private String version = "1";
    @Column(length = 30, nullable = false)
    private String account = "";
    private LocalDateTime date;
    @Column(length = 50, nullable = false)
    private String mark = "";
    @Column(length = 255, nullable = false)
    private String extra = "";
}
