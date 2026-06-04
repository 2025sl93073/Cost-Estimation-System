package com.ces.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 20)
    private String unit;  // e.g., kg, bags, cubic meters

    @Column(name = "base_price_per_unit", nullable = false)
    private Double basePricePerUnit;

    @Column(length = 50)
    private String category; // cement, steel, sand, bricks, etc.
}
