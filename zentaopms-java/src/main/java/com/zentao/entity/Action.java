package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作记录实体 - 对应 zt_action
 */
@Data
@Entity
@Table(name = "zt_action")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String objectType;
    private Integer objectID = 0;
    @Column(columnDefinition = "text")
    private String product;
    private Integer project = 0;
    private Integer execution = 0;
    @Column(length = 30)
    private String actor;
    @Column(length = 80)
    private String action;
    private LocalDateTime date;
    @Column(columnDefinition = "text")
    private String comment;
    @Column(columnDefinition = "text")
    private String files;
    @Column(columnDefinition = "text")
    private String extra;
    @Column(name = "`read`")
    private Integer read_ = 0;
    private Integer efforted = 0;
    @Column(length = 10)
    private String vision = "rnd";
}
