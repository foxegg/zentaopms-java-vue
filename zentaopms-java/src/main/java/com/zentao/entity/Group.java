package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 权限组实体 - 对应 zt_group
 */
@Data
@Entity
@Table(name = "zt_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    @Column(length = 30)
    private String name;
    @Column(length = 30)
    private String role;
    @Column(name = "`desc`", length = 255)
    private String description;
    @Column(columnDefinition = "text")
    private String acl;
    private Integer developer = 1;
    @Column(length = 10)
    private String vision = "rnd";
}
