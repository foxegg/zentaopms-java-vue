package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 定时任务实体 - 对应 zt_cron
 */
@Data
@Entity
@Table(name = "zt_cron")
public class Cron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String m = "";
    @Column(length = 20)
    private String h = "";
    @Column(length = 20)
    private String dom = "";
    @Column(length = 20)
    private String mon = "";
    @Column(length = 20)
    private String dow = "";
    @Column(columnDefinition = "text")
    private String command;
    @Column(length = 255)
    private String remark;
    @Column(length = 20)
    private String type;
    private Integer buildin = 0;
    @Column(length = 20)
    private String status;
    private LocalDateTime lastTime;
}
