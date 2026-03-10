package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统操作日志 - 对应 zt_log（API/请求日志等）
 */
@Data
@Entity
@Table(name = "zt_log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "objectType", length = 30, nullable = false)
    private String objectType = "";

    @Column(name = "objectID")
    private Integer objectID = 0;

    private Integer action = 0;

    private LocalDateTime date;

    @Column(length = 255)
    private String url = "";

    @Column(name = "contentType", length = 30)
    private String contentType = "";

    @Column(columnDefinition = "text")
    private String data;

    @Column(columnDefinition = "text")
    private String result;
}
