package com.personnel.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "selection_result")
public class SelectionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "record_id", nullable = false)
    @JsonIgnoreProperties({"results"})
    private SelectionRecord record;

    @ManyToOne
    @JoinColumn(name = "personnel_id", nullable = false)
    @JsonIgnoreProperties({"selectionResults"})
    private Personnel personnel;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
