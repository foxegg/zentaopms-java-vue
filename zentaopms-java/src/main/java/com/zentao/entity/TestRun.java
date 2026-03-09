package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 测试单-用例执行记录 - 对应 zt_testrun
 */
@Data
@Entity
@Table(name = "zt_testrun")
public class TestRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "`task`")
    private Integer taskId = 0;
    @Column(name = "`case`")
    private Integer caseId = 0;
    private Integer version = 1;
    @Column(length = 30)
    private String assignedTo;
    @Column(length = 30)
    private String lastRunner;
    private LocalDateTime lastRunDate;
    @Column(length = 30)
    private String lastRunResult;
    @Column(length = 30)
    private String status;
}
