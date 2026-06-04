package com.ces.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cost_estimations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostEstimation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, unique = true)
    private ConstructionProject project;

    @Column(name = "material_cost", nullable = false)
    private Double materialCost;

    @Column(name = "labour_cost", nullable = false)
    private Double labourCost;

    @Column(name = "labour_force_count")
    private Integer labourForceCount;

    @Column(name = "time_factor_cost")
    private Double timeFactorCost;

    @Column(name = "quality_factor_cost")
    private Double qualityFactorCost;

    @Column(name = "total_cost", nullable = false)
    private Double totalCost;

    @Column(name = "estimated_at", updatable = false)
    private LocalDateTime estimatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimated_by")
    private User estimatedBy;

    @PrePersist
    protected void onCreate() {
        estimatedAt = LocalDateTime.now();
    }
}
