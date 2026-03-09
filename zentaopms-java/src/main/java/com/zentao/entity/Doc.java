package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_doc")
public class Doc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private Integer product = 0;
    private Integer execution = 0;
    private Integer lib = 0;
    @Column(length = 30)
    private String template;
    @Column(length = 30)
    private String templateType;
    @Column(columnDefinition = "text")
    private String templateDesc;
    @Column(columnDefinition = "text")
    private String objects;
    @Column(length = 30)
    private String chapterType;
    @Column(length = 10)
    private String cycle;
    @Column(columnDefinition = "text")
    private String cycleConfig;
    private Integer module = 0;
    @Column(length = 20)
    private String reportModule;
    @Column(length = 255)
    private String title;
    @Column(length = 255)
    private String keywords;
    @Column(length = 30)
    private String type;
    @Column(length = 30)
    private String status = "normal";
    private Integer parent = 0;
    @Column(length = 255)
    private String path;
    private Integer grade = 0;
    @Column(name = "`order`")
    private Integer orderNum = 0;
    private Integer views = 0;
    private Integer assetLib = 0;
    @Column(length = 30)
    private String assetLibType;
    @Column(name = "`from`")
    private Integer fromDoc = 0;
    private Integer fromVersion = 1;
    @Column(columnDefinition = "longtext")
    private String draft;
    private Integer collects = 0;
    @Column(length = 8)
    private String weeklyDate;
    @Column(length = 30)
    private String addedBy;
    private LocalDateTime addedDate;
    @Column(length = 30)
    private String assignedTo;
    private LocalDateTime assignedDate;
    private LocalDateTime approvedDate;
    private Integer deleted = 0;
}
