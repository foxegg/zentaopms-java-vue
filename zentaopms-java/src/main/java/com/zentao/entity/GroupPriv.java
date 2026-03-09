package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "zt_grouppriv")
public class GroupPriv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "`group`")
    private Integer groupId = 0;
    @Column(length = 30)
    private String module;
    @Column(length = 30)
    private String method;
}
