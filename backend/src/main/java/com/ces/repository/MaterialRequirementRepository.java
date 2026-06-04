package com.ces.repository;

import com.ces.entity.MaterialRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaterialRequirementRepository extends JpaRepository<MaterialRequirement, Long> {
    List<MaterialRequirement> findByProjectId(Long projectId);
    void deleteByProjectId(Long projectId);
}
