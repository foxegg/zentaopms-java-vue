package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 构建任务实体 - 对应 zt_job (Jenkins/CI)
 */
@Data
@Entity
@Table(name = "zt_job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;
    private Integer repo = 0;
    private Integer product = 0;
    @Column(length = 20)
    private String frame;
    @Column(length = 20)
    private String engine;
    private Integer autoRun = 1;
    private Integer server = 0;
    @Column(length = 500)
    private String pipeline;
    @Column(length = 255)
    private String triggerType;
    private Integer sonarqubeServer = 0;
    @Column(length = 255)
    private String projectKey;
    @Column(length = 255)
    private String svnDir;
    @Column(length = 255)
    private String atDay;
    @Column(length = 10)
    private String atTime;
    @Column(columnDefinition = "text")
    private String customParam;
    @Column(length = 255)
    private String comment;
    @Column(length = 255)
    private String triggerActions;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String editedBy;
    private LocalDateTime editedDate;
    private LocalDateTime lastExec;
    @Column(length = 255)
    private String lastStatus;
    @Column(length = 255)
    private String lastTag;
    private LocalDateTime lastSyncDate;
    private Integer deleted = 0;
}
