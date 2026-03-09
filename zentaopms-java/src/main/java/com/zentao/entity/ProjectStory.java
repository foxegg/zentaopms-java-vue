package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 项目需求关联 - 对应 zt_projectstory
 */
@Data
@Entity
@Table(name = "zt_projectstory")
public class ProjectStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private Integer product = 0;
    private Integer branch = 0;
    private Integer story = 0;
    private Integer version = 1;
    @Column(name = "`order`")
    private Integer orderNum = 0;
}
