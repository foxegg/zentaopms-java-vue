package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 部门实体 - 对应 zt_dept
 */
@Data
@Entity
@Table(name = "zt_dept")
public class Dept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 60)
    private String name;
    private Integer parent = 0;
    @Column(length = 255)
    private String path;
    private Integer grade = 0;
    @Column(name = "`order`")
    private Integer orderNum = 0;
    @Column(length = 30)
    private String manager;
}
