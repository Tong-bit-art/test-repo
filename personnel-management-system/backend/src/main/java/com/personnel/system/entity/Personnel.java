package com.personnel.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "personnel")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Personnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private Integer age;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"personnel"})
    private Category category;

    @Column(name = "department")
    private String department;

    @Column(name = "position")
    private String position;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    @JsonIgnoreProperties({"personnel"})
    private User createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
