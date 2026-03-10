package com.zentao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/** 对应 zt_ai_agent（Prompt/智能体） */
@Data
@Entity
@Table(name = "zt_ai_agent")
public class AiAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false)
    private String code = "";
    @Column(length = 20, nullable = false)
    private String name = "";
    @Column(name = "`desc`", columnDefinition = "text")
    private String description;
    @Column(length = 255, nullable = false)
    private String model = "";
    @Column(name = "knowledgeLib", length = 255, nullable = false)
    private String knowledgeLib = "";
    @Column(length = 30, nullable = false)
    private String module = "";
    @Column(columnDefinition = "text")
    private String source;
    @Column(length = 30, nullable = false)
    private String targetForm = "";
    @Column(columnDefinition = "text")
    private String purpose;
    @Column(columnDefinition = "text")
    private String elaboration;
    @Column(columnDefinition = "text")
    private String role;
    @Column(columnDefinition = "text")
    private String characterization;
    @Column(length = 10, nullable = false)
    private String status = "draft";
    @Column(length = 30, nullable = false)
    private String createdBy = "";
    private LocalDateTime createdDate;
    @Column(length = 30, nullable = false)
    private String editedBy = "";
    private LocalDateTime editedDate;
    private Integer deleted = 0;
}
