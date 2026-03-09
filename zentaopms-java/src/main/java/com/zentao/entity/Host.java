package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/** 主机 - 对应 zt_host */
@Data
@Entity
@Table(name = "zt_host")
public class Host {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 255, nullable = false)
    private String name = "";
    @Column(length = 30, nullable = false)
    private String type = "normal";
    @Column(length = 30)
    private String hostType = "";
    @Column(length = 128)
    private String mac = "";
    @Column(length = 30)
    private String memory = "";
    @Column(length = 30)
    private String diskSize = "";
    @Column(length = 50)
    private String status = "";
    @Column(length = 50)
    private String secret = "";
    @Column(name = "`desc`", columnDefinition = "text")
    private String hostDesc;
    @Column(length = 50)
    private String tokenSN = "";
    private LocalDateTime tokenTime;
    @Column(length = 50)
    private String oldTokenSN = "";
    @Column(length = 30)
    private String vsoft = "";
    private LocalDateTime heartbeat;
    @Column(length = 10)
    private String zap = "";
    private Integer vnc = 0;
    private Integer ztf = 0;
    private Integer zd = 0;
    private Integer ssh = 0;
    private Integer parent = 0;
    private Integer image = 0;
    private Integer admin = 0;
    private Integer serverRoom = 0;
    @Column(length = 16)
    private String cpuNumber = "";
    @Column(length = 30)
    private String cpuCores = "";
    @Column(length = 128)
    private String intranet = "";
    @Column(length = 128)
    private String extranet = "";
    @Column(length = 64)
    private String osName = "";
    @Column(length = 64)
    private String osVersion = "";
    @Column(name = "`group`", length = 128)
    private String hostGroup = "";
    @Column(length = 30)
    private String createdBy = "";
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String editedBy = "";
    private LocalDateTime editedDate;
    private Integer deleted = 0;
}
