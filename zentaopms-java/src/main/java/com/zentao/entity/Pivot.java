package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据透视实体 - 对应 zt_pivot（与 PHP pivot 模块一致）
 */
@Data
@Entity
@Table(name = "zt_pivot")
public class Pivot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer dimension = 0;
    @Column(name = "`group`", length = 255, nullable = false)
    private String group = "";
    @Column(length = 255, nullable = false)
    private String code = "";
    @Column(length = 10, nullable = false)
    private String driver = "mysql";
    @Column(length = 10, nullable = false)
    private String mode = "builder";
    @Column(columnDefinition = "text")
    private String name;
    @Column(name = "`desc`", columnDefinition = "text")
    private String description;
    @Column(length = 10, nullable = false)
    private String acl = "open";
    @Column(columnDefinition = "text")
    private String whitelist;
    @Column(columnDefinition = "text")
    private String sql;
    @Column(columnDefinition = "text")
    private String fields;
    @Column(columnDefinition = "text")
    private String langs;
    @Column(columnDefinition = "text")
    private String vars;
    @Column(columnDefinition = "text")
    private String objects;
    @Column(columnDefinition = "text")
    private String settings;
    @Column(columnDefinition = "text")
    private String filters;
    private Integer step = 0;
    @Column(length = 10, nullable = false)
    private String stage = "draft";
    private Integer builtin = 0;
    @Column(length = 10, nullable = false)
    private String version = "1";
    @Column(length = 30, nullable = false)
    private String createdBy = "";
    private LocalDateTime createdDate;
    @Column(length = 30, nullable = false)
    private String editedBy = "";
    private LocalDateTime editedDate;
    private Integer deleted = 0;
}
