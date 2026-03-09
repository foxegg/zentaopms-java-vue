package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_testsuite")
public class TestSuite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private Integer product = 0;
    @Column(length = 255)
    private String name;
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    @Column(length = 20)
    private String type;
    @Column(name = "`order`")
    private Integer orderNum = 0;
    @Column(length = 30)
    private String addedBy;
    private LocalDateTime addedDate;
    @Column(length = 30)
    private String lastEditedBy;
    private LocalDateTime lastEditedDate;
    private Integer deleted = 0;
}
