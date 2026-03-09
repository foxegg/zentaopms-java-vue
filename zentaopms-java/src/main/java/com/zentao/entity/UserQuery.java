package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 用户保存的搜索条件 - 对应 zt_userquery
 */
@Data
@Entity
@Table(name = "zt_userquery")
public class UserQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String account;
    @Column(length = 30)
    private String module;
    @Column(length = 90)
    private String title;
    @Column(columnDefinition = "text")
    private String form;
    @Column(columnDefinition = "text")
    private String sql;
    private Integer shortcut = 0;
    private Integer common = 0;
}
