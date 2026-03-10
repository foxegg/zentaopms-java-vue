package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应用实例实体 - 对应 zt_instance（与 PHP instance 模块一致）
 */
@Data
@Entity
@Table(name = "zt_instance")
public class Instance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer space = 0;
    private Integer solution = 0;
    @Column(length = 50, nullable = false)
    private String name = "";
    private Integer appID = 0;
    @Column(length = 50)
    private String appName = "";
    @Column(length = 20)
    private String appVersion = "";
    @Column(length = 50)
    private String chart = "";
    @Column(length = 255)
    private String logo = "";
    @Column(length = 50)
    private String version = "";
    @Column(name = "`desc`", columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String introduction;
    @Column(length = 20)
    private String source = "";
    @Column(length = 20)
    private String channel = "";
    @Column(length = 64)
    private String k8name = "";
    @Column(length = 20)
    private String status = "";
    private Integer pinned = 0;
    @Column(length = 255)
    private String domain = "";
    @Column(length = 30)
    private String smtpSnippetName = "";
    @Column(length = 30)
    private String ldapSnippetName = "";
    @Column(columnDefinition = "text")
    private String ldapSettings;
    @Column(columnDefinition = "text")
    private String dbSettings;
    private Integer autoBackup = 0;
    private Integer backupKeepDays = 1;
    private Integer autoRestore = 0;
    @Column(columnDefinition = "text")
    private String env;
    @Column(length = 30)
    private String createdBy = "";
    private LocalDateTime createdAt;
    private Integer deleted = 0;
}
