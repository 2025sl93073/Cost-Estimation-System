package com.ces.service;

import com.ces.dto.MaterialDto;
import java.util.List;

public interface MaterialService {
    List<MaterialDto.Response> getAllMaterials();
    MaterialDto.Response getMaterialById(Long id);
    MaterialDto.Response createMaterial(MaterialDto.CreateRequest request);
    MaterialDto.Response updateMaterial(Long id, MaterialDto.UpdateRequest request);
    void deleteMaterial(Long id);
    void updateMaterialCost(MaterialDto.CostUpdateRequest request, Long updatedByUserId);
    List<MaterialDto.Response> getMaterialsByCategory(String category);
}
