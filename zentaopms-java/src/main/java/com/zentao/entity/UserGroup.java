package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 用户-权限组关联 - 对应 zt_usergroup
 */
@Data
@Entity
@Table(name = "zt_usergroup")
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String account;
    @Column(name = "`group`")
    private Integer groupId = 0;
    @Column(columnDefinition = "text")
    private String project;
}
