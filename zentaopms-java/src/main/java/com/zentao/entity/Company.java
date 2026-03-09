package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 公司/组织实体 - 对应 zt_company
 */
@Data
@Entity
@Table(name = "zt_company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 120)
    private String name;
    @Column(length = 20)
    private String phone;
    @Column(length = 20)
    private String fax;
    @Column(length = 120)
    private String address;
    @Column(length = 10)
    private String zipcode;
    @Column(length = 120)
    private String website;
    @Column(length = 120)
    private String backyard;
    private Integer guest = 0;
    @Column(length = 255)
    private String admins;
    private Integer deleted = 0;
}
