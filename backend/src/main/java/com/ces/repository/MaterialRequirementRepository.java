package com.ces.repository;

import com.ces.entity.MaterialRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MaterialRequirementRepository extends JpaRepository<MaterialRequirement, Long> {
    List<MaterialRequirement> findByProjectId(Long projectId);

    @Modifying
    @Query("DELETE FROM MaterialRequirement mr WHERE mr.project.id = :projectId")
    void deleteByProjectId(Long projectId);
}
