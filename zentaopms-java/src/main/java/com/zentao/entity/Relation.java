package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 关系表，与 PHP zt_relation 一致；用于需求关联等（linkedto/linkedfrom）。
 */
@Data
@Entity
@Table(name = "zt_relation")
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private Integer product = 0;
    private Integer execution = 0;

    @Column(name = "AType", length = 30, nullable = false)
    private String aType = "";

    @Column(name = "AID")
    private Integer aID = 0;

    @Column(name = "AVersion", length = 30, nullable = false)
    private String aVersion = "";

    @Column(length = 30, nullable = false)
    private String relation = "";

    @Column(name = "BType", length = 30, nullable = false)
    private String bType = "";

    @Column(name = "BID")
    private Integer bID = 0;

    @Column(name = "BVersion", length = 30, nullable = false)
    private String bVersion = "";

    @Column(length = 30, nullable = false)
    private String extra = "";
}
