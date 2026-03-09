package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体 - 对应 zt_user
 */
@Data
@Entity
@Table(name = "zt_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer company = 0;
    @Column(length = 30)
    private String type = "inside";
    private Integer dept = 0;
    @Column(length = 30, unique = true)
    private String account;
    @Column(length = 32)
    private String password;
    @Column(length = 10)
    private String role;
    @Column(length = 100)
    private String realname;
    @Column(length = 30)
    private String superior;
    @Column(length = 255)
    private String pinyin;
    @Column(length = 60)
    private String nickname;
    @Column(length = 100)
    private String commiter;
    @Column(columnDefinition = "text")
    private String avatar;
    private LocalDate birthday;
    @Column(length = 1)
    private String gender = "f";
    @Column(length = 90)
    private String email;
    @Column(length = 90)
    private String skype;
    @Column(length = 20)
    private String qq;
    @Column(length = 20)
    private String mobile;
    @Column(length = 20)
    private String phone;
    @Column(length = 90)
    private String weixin;
    @Column(length = 90)
    private String dingding;
    @Column(length = 90)
    private String slack;
    @Column(length = 90)
    private String whatsapp;
    @Column(length = 120)
    private String address;
    @Column(length = 10)
    private String zipcode;
    @Column(columnDefinition = "text")
    private String nature;
    @Column(columnDefinition = "text")
    private String analysis;
    @Column(columnDefinition = "text")
    private String strategy;
    @Column(name = "`join`")
    private LocalDate joinDate;
    private Integer visits = 0;
    @Column(length = 20)
    private String visions = "rnd,lite";
    @Column(length = 255)
    private String ip;
    private LocalDateTime last;
    private Integer fails = 0;
    private LocalDateTime locked;
    private Integer feedback = 0;
    @Column(length = 30)
    private String ranzhi;
    @Column(length = 30)
    private String ldap;
    private Integer score = 0;
    private Integer scoreLevel = 0;
    @Column(length = 50)
    private String resetToken;
    @Column(length = 10)
    private String clientStatus = "offline";
    @Column(length = 10)
    private String clientLang = "zh-cn";
    private Integer deleted = 0;
}
