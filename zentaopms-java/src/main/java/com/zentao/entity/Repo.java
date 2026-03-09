package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 代码库实体 - 对应 zt_repo
 */
@Data
@Entity
@Table(name = "zt_repo")
public class Repo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255)
    private String product = "";
    @Column(length = 255)
    private String projects = "";
    @Column(length = 255)
    private String name;
    @Column(length = 255)
    private String path;
    @Column(length = 100)
    private String prefix;
    @Column(length = 20)
    private String encoding;
    @Column(name = "SCM", length = 10)
    private String scm;
    @Column(length = 100)
    private String client;
    @Column(length = 50)
    private String serviceHost;
    @Column(length = 100)
    private String serviceProject;
    private Integer commits = 0;
    @Column(length = 30)
    private String account;
    @Column(length = 30)
    private String password;
    @Column(length = 30)
    private String encrypt = "plain";
    @Column(columnDefinition = "text")
    private String acl;
    private Integer synced = 0;
    private LocalDateTime lastSync;
    private LocalDateTime lastCommit;
    @Column(name = "`desc`", columnDefinition = "text")
    private String description;
    @Column(length = 30)
    private String extra;
    private Integer preMerge = 0;
    private Integer job = 0;
    @Column(columnDefinition = "text")
    private String fileServerUrl;
    @Column(length = 40)
    private String fileServerAccount;
    @Column(length = 100)
    private String fileServerPassword;
    private Integer deleted = 0;
}
