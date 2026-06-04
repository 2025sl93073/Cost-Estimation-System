package com.ces.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "material_costs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(nullable = false)
    private Double price;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "fluctuation_percent")
    private Double fluctuationPercent; // % change from previous

    @Column(length = 100)
    private String region;

    @Column(name = "updated_by")
    private Long updatedBy;
}
