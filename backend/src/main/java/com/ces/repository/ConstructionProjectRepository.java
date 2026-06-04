package com.ces.repository;

import com.ces.entity.ConstructionProject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConstructionProjectRepository extends JpaRepository<ConstructionProject, Long> {
    List<ConstructionProject> findByCreatedById(Long userId);
    List<ConstructionProject> findByCity(String city);
    List<ConstructionProject> findByConstructionType(ConstructionProject.ConstructionType type);
}
