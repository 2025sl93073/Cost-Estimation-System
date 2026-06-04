package com.ces.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "material_requirements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private ConstructionProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(nullable = false)
    private Double quantity;

    @Column(name = "estimated_cost")
    private Double estimatedCost;
}
