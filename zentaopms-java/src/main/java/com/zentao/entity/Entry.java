package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/** 应用入口 - 对应 zt_entry */
@Data
@Entity
@Table(name = "zt_entry")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 50, nullable = false)
    private String name = "";
    @Column(length = 30)
    private String account = "";
    @Column(length = 20)
    private String code = "";
    @Column(name = "`key`", length = 32)
    private String keyStr = "";
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    private Integer deleted = 0;
    private LocalDateTime createdDate;
    private LocalDateTime editedDate;
}
