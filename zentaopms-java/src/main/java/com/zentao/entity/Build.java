package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_build")
public class Build {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private Integer product = 0;
    @Column(length = 255)
    private String branch = "0";
    private Integer execution = 0;
    @Column(length = 255)
    private String builds;
    @Column(length = 150)
    private String name;
    private Integer system = 0;
    @Column(length = 255)
    private String scmPath;
    @Column(length = 255)
    private String filePath;
    private LocalDate date;
    @Column(columnDefinition = "text")
    private String stories;
    @Column(columnDefinition = "text")
    private String bugs;
    private Integer artifactRepoID = 0;
    @Column(length = 30)
    private String builder;
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    private Integer deleted = 0;
}
