package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 扩展/插件实体 - 对应 zt_extension
 */
@Data
@Entity
@Table(name = "zt_extension")
public class Extension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 150)
    private String name;
    @Column(length = 30)
    private String code;
    @Column(length = 50)
    private String version;
    @Column(length = 100)
    private String author;
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    @Column(columnDefinition = "text")
    private String license;
    @Column(length = 20)
    private String type = "extension";
    @Column(length = 150)
    private String site;
    @Column(columnDefinition = "text")
    private String zentaoCompatible;
    private LocalDateTime installedTime;
    @Column(length = 100)
    private String depends;
    @Column(columnDefinition = "mediumtext")
    private String dirs;
    @Column(columnDefinition = "mediumtext")
    private String files;
    @Column(length = 20)
    private String status;
}
