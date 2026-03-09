package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "zt_projectproduct")
public class ProjectProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer project = 0;
    private Integer product = 0;
    private Integer branch = 0;
    @Column(length = 255)
    private String plan;
    @Column(length = 255)
    private String roadmap;
}
