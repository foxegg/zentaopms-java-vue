package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 仪表盘区块 - 对应 zt_block
 */
@Data
@Entity
@Table(name = "zt_block")
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String account;
    @Column(length = 20)
    private String dashboard;
    @Column(length = 20)
    private String module;
    @Column(length = 100)
    private String title;
    @Column(length = 30)
    private String block;
    @Column(length = 30)
    private String code;
    private Integer width = 1;
    private Integer height = 3;
    @Column(name = "`left`")
    private Integer leftPos = 0;
    @Column(name = "`top`")
    private Integer topPos = 0;
    @Column(columnDefinition = "text")
    private String params;
    private Integer hidden = 0;
    @Column(length = 10)
    private String vision = "rnd";
}
