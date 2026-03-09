package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_webhook")
public class Webhook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 15)
    private String type = "default";
    @Column(length = 50)
    private String name;
    @Column(length = 255)
    private String url;
    @Column(length = 255)
    private String domain;
    @Column(length = 255)
    private String secret;
    @Column(length = 30)
    private String contentType = "application/json";
    @Column(length = 10)
    private String sendType = "sync";
    @Column(columnDefinition = "text")
    private String products;
    @Column(columnDefinition = "text")
    private String executions;
    @Column(length = 100)
    private String params;
    @Column(columnDefinition = "text")
    private String actions;
    @Column(columnDefinition = "text")
    private String description;
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String editedBy;
    private LocalDateTime editedDate;
    private Integer deleted = 0;
}
