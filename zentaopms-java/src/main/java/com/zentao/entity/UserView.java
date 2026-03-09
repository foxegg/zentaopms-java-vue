package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 用户视图权限 - 对应 zt_userview
 */
@Data
@Entity
@Table(name = "zt_userview")
public class UserView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, unique = true)
    private String account;
    @Column(columnDefinition = "mediumtext")
    private String programs;
    @Column(columnDefinition = "mediumtext")
    private String products;
    @Column(columnDefinition = "mediumtext")
    private String projects;
    @Column(columnDefinition = "mediumtext")
    private String sprints;
}
