package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "zt_feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer product = 0;
    @Column(length = 255, nullable = false)
    private String title = "";
    @Column(length = 30)
    private String type = "";
    @Column(length = 30)
    private String status = "";
    @Column(length = 30)
    private String openedBy;
    private LocalDateTime openedDate;
    @Column(length = 30)
    private String editedBy;
    private LocalDateTime editedDate;
    @Column(length = 30)
    private String assignedTo;
    private Integer deleted = 0;
}
