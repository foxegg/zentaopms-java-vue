package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "zt_acl")
public class Acl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String account;
    @Column(length = 30)
    private String objectType;
    private Integer objectID = 0;
    @Column(length = 40)
    private String type = "whitelist";
    @Column(length = 30)
    private String source;
}
