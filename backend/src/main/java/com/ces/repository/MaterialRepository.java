package com.ces.repository;

import com.ces.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByCategory(String category);
    Optional<Material> findByName(String name);
    boolean existsByName(String name);
}
