package com.personnel.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "selection_record")
public class SelectionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"selectionRecords"})
    private User user;

    @Column(name = "selection_type", nullable = false)
    private String selectionType;

    @Column(name = "criteria", columnDefinition = "TEXT")
    private String criteria;

    @Column(name = "selected_count", nullable = false)
    private Integer selectedCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
    private List<SelectionResult> results;
}
