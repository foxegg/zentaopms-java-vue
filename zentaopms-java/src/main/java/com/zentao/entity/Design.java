package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_design")
public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer project = 0;
    private Integer product = 0;
    private Integer execution = 0;
    @Column(length = 255, nullable = false)
    private String name = "";
    @Column(length = 30)
    private String status = "";
    @Column(length = 30)
    private String story = "";
    @Column(length = 30)
    private String type = "";
    @Column(length = 30)
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(length = 30)
    private String editedBy;
    private LocalDateTime editedDate;
    @Column(length = 30)
    private String assignedTo;
    private Integer deleted = 0;
}
