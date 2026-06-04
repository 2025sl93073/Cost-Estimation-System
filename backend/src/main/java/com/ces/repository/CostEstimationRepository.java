package com.ces.repository;

import com.ces.entity.CostEstimation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface CostEstimationRepository extends JpaRepository<CostEstimation, Long> {
    Optional<CostEstimation> findByProjectId(Long projectId);

    @Query("SELECT ce FROM CostEstimation ce WHERE ce.estimatedBy.id = :userId")
    List<CostEstimation> findByEstimatedByUserId(Long userId);

    @Query("SELECT SUM(ce.totalCost) FROM CostEstimation ce")
    Double sumTotalCost();
}
