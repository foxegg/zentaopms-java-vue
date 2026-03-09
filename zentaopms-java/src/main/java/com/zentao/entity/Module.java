package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "zt_module")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer root = 0;
    private Integer branch = 0;
    @Column(length = 60)
    private String name;
    private Integer parent = 0;
    @Column(length = 255)
    private String path;
    private Integer grade = 0;
    @Column(name = "`order`")
    private Integer orderNum = 0;
    @Column(length = 30)
    private String type;
    @Column(name = "`from`")
    private Integer fromModule = 0;
    @Column(length = 30)
    private String owner;
    @Column(columnDefinition = "text")
    private String collector;
    @Column(length = 60)
    private String short_;
    @Column(length = 30)
    private String extra;
    private Integer deleted = 0;
}
