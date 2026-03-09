package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "zt_suitecase")
public class SuiteCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer suite = 0;
    private Integer product = 0;
    @Column(name = "`case`")
    private Integer caseId = 0;
    private Integer version = 1;
}
