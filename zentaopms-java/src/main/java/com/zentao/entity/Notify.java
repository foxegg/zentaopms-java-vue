package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知队列实体 - 对应 zt_notify (mail/消息)
 */
@Data
@Entity
@Table(name = "zt_notify")
public class Notify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 10)
    private String objectType;
    private Integer objectID = 0;
    private Integer action = 0;
    @Column(columnDefinition = "text")
    private String toList;
    @Column(columnDefinition = "text")
    private String ccList;
    @Column(columnDefinition = "text")
    private String subject;
    @Column(columnDefinition = "text")
    private String data;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    private LocalDateTime sendTime;
    @Column(length = 10)
    private String status = "wait";
    @Column(columnDefinition = "text")
    private String failReason;
}
