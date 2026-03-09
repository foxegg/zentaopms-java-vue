package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_doclib")
public class DocLib {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String type;
    private Integer parent = 0;
    private Integer product = 0;
    private Integer project = 0;
    private Integer execution = 0;
    @Column(length = 60)
    private String name;
    @Column(length = 255)
    private String baseUrl;
    @Column(length = 10)
    private String acl = "open";
    @Column(length = 255)
    private String groups;
    @Column(columnDefinition = "text")
    private String users;
    private Integer main = 0;
    @Column(columnDefinition = "text")
    private String collector;
    @Column(name = "`desc`", columnDefinition = "mediumtext")
    private String description;
    @Column(name = "`order`")
    private Integer orderNum = 0;
    @Column(length = 30)
    private String addedBy;
    private LocalDateTime addedDate;
    private Integer archived = 0;
    @Column(length = 30)
    private String orderBy = "id_asc";
    @Column(length = 10)
    private String vision = "rnd";
    private Integer deleted = 0;
}
