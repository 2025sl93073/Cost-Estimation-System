package com.ces.repository;

import com.ces.entity.MaterialCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface MaterialCostRepository extends JpaRepository<MaterialCost, Long> {
    List<MaterialCost> findByMaterialIdOrderByEffectiveDateDesc(Long materialId);

    @Query("SELECT mc FROM MaterialCost mc WHERE mc.material.id = :materialId ORDER BY mc.id DESC")
    List<MaterialCost> findLatestByMaterialIdList(Long materialId);

    default Optional<MaterialCost> findLatestByMaterialId(Long materialId) {
        List<MaterialCost> results = findLatestByMaterialIdList(materialId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    List<MaterialCost> findByRegionOrderByEffectiveDateDesc(String region);
}
