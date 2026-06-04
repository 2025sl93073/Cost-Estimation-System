package com.ces.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "construction_projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstructionProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "construction_type", nullable = false)
    private ConstructionType constructionType; // COMMERCIAL, RESIDENTIAL

    @Enumerated(EnumType.STRING)
    @Column(name = "type1", nullable = false)
    private Type1 type1; // BUSINESS, HOUSING

    @Enumerated(EnumType.STRING)
    @Column(name = "type2", nullable = false)
    private Type2 type2; // COMMERCIAL_ESTABLISHMENT, RESIDENTIAL

    @Enumerated(EnumType.STRING)
    @Column(name = "structure_type", nullable = false)
    private StructureType structureType; // COMPLEX, BRIDGE, HOUSE, FLYOVER, OTHER

    @Column(nullable = false, length = 150)
    private String area;

    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(name = "area_sqft", nullable = false)
    private Double areaSqft;

    @Column(name = "time_required_months", nullable = false)
    private Integer timeRequiredMonths;

    @Column(name = "quality_factor", nullable = false)
    private Double qualityFactor; // 1.0 = standard, 1.5 = premium

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum ConstructionType { COMMERCIAL, RESIDENTIAL }
    public enum Type1 { BUSINESS, HOUSING }
    public enum Type2 { COMMERCIAL_ESTABLISHMENT, RESIDENTIAL }
    public enum StructureType { COMPLEX, BRIDGE, HOUSE, FLYOVER, OTHER }
}
