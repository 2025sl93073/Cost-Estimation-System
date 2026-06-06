package com.ces.service.impl;

import com.ces.dto.MaterialDto;
import com.ces.entity.Material;
import com.ces.entity.MaterialCost;
import com.ces.repository.MaterialCostRepository;
import com.ces.repository.MaterialRepository;
import com.ces.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired private MaterialRepository materialRepository;
    @Autowired private MaterialCostRepository materialCostRepository;

    @Override
    public List<MaterialDto.Response> getAllMaterials() {
        return materialRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public MaterialDto.Response getMaterialById(Long id) {
        Material m = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found: " + id));
        return toResponse(m);
    }

    @Override
    public MaterialDto.Response createMaterial(MaterialDto.CreateRequest request) {
        if (materialRepository.existsByName(request.getName())) {
            throw new RuntimeException("Material already exists: " + request.getName());
        }
        Material material = Material.builder()
                .name(request.getName())
                .description(request.getDescription())
                .unit(request.getUnit())
                .basePricePerUnit(0.0)
                .category(request.getCategory())
                .build();
        return toResponse(materialRepository.save(material));
    }

    @Override
    public MaterialDto.Response updateMaterial(Long id, MaterialDto.UpdateRequest request) {
        Material m = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found: " + id));
        if (request.getName() != null) m.setName(request.getName());
        if (request.getDescription() != null) m.setDescription(request.getDescription());
        if (request.getUnit() != null) m.setUnit(request.getUnit());
        if (request.getCategory() != null) m.setCategory(request.getCategory());
        return toResponse(materialRepository.save(m));
    }

    @Override
    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateMaterialCost(MaterialDto.CostUpdateRequest request, Long updatedByUserId) {
        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new RuntimeException("Material not found"));

        material.setBasePricePerUnit(request.getPrice());
        materialRepository.save(material);

        java.time.LocalDate effectiveDate = request.getEffectiveDate() != null
                ? request.getEffectiveDate()
                : java.time.LocalDate.now();

        MaterialCost cost = MaterialCost.builder()
                .material(material)
                .price(request.getPrice())
                .effectiveDate(effectiveDate)
                .region(request.getRegion())
                .updatedBy(updatedByUserId)
                .build();
        materialCostRepository.save(cost);
    }

    @Override
    public List<MaterialDto.Response> getMaterialsByCategory(String category) {
        return materialRepository.findByCategory(category).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private MaterialDto.Response toResponse(Material m) {
        Double latestPrice = materialCostRepository.findLatestByMaterialId(m.getId())
                .map(MaterialCost::getPrice)
                .orElse(m.getBasePricePerUnit());
        return new MaterialDto.Response(m.getId(), m.getName(), m.getDescription(),
                m.getUnit(), m.getBasePricePerUnit(), m.getCategory(), latestPrice);
    }
}
