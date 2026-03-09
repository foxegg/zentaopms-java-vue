package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String pathname;
    @Column(length = 255)
    private String title;
    @Column(length = 30)
    private String extension;
    private Integer size = 0;
    @Column(length = 30)
    private String objectType;
    private Integer objectID = 0;
    @Column(length = 48)
    private String gid;
    @Column(length = 30)
    private String addedBy;
    private LocalDateTime addedDate;
    private Integer downloads = 0;
    @Column(length = 255)
    private String extra;
    private Integer deleted = 0;
}
