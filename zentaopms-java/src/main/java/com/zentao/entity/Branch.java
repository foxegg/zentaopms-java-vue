package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 产品分支 - 对应 zt_branch
 */
@Data
@Entity
@Table(name = "zt_branch")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer product = 0;
    @Column(length = 255)
    private String name;
    @Column(name = "`default`")
    private Integer defaultBranch = 0;
    @Column(length = 10)
    private String status = "active";
    @Column(name = "`desc`", length = 255)
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime closedDate;
    @Column(name = "`order`")
    private Integer orderNum = 0;
    private Integer deleted = 0;
}
