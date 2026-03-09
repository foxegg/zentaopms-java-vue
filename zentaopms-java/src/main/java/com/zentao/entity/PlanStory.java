package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 计划-需求关联 - 对应 zt_planstory
 */
@Data
@Entity
@Table(name = "zt_planstory")
public class PlanStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer plan = 0;
    private Integer story = 0;
    @Column(name = "`order`")
    private Integer orderNum = 0;
}
