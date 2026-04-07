package com.personnel.system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "operation_log")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "operation_type", nullable = false)
    private String operationType;

    @Column(name = "operation_description", nullable = false)
    private String operationDescription;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "request_url")
    private String requestUrl;

    @Column(name = "request_params")
    private String requestParams;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "execution_time")
    private Long executionTime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}