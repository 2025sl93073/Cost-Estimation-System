package com.ces.service.impl;

import com.ces.dto.CostEstimationDto;
import com.ces.entity.*;
import com.ces.repository.*;
import com.ces.service.CostEstimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CostEstimationServiceImpl implements CostEstimationService {

    @Autowired private CostEstimationRepository estimationRepository;
    @Autowired private ConstructionProjectRepository projectRepository;
    @Autowired private MaterialRepository materialRepository;
    @Autowired private MaterialCostRepository materialCostRepository;
    @Autowired private MaterialRequirementRepository requirementRepository;
    @Autowired private UserRepository userRepository;

    // Labour cost per person per month (base)
    private static final double LABOUR_RATE_PER_PERSON_PER_MONTH = 15000.0;

    @Override
    @Transactional
    public CostEstimationDto.Response estimate(CostEstimationDto.EstimateRequest request, String username) {
        ConstructionProject project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // --- Material Cost ---
        double materialCost = 0.0;
        requirementRepository.deleteByProjectId(project.getId());
        List<MaterialRequirement> requirements = new ArrayList<>();

        if (request.getMaterials() != null) {
            for (CostEstimationDto.MaterialItem item : request.getMaterials()) {
                Material material = materialRepository.findById(item.getMaterialId())
                        .orElseThrow(() -> new RuntimeException("Material not found: " + item.getMaterialId()));
                double unitPrice = materialCostRepository
                        .findLatestByMaterialId(material.getId())
                        .map(MaterialCost::getPrice)
                        .orElse(material.getBasePricePerUnit());
                double cost = unitPrice * item.getQuantity();
                materialCost += cost;
                requirements.add(MaterialRequirement.builder()
                        .project(project).material(material)
                        .quantity(item.getQuantity()).estimatedCost(cost)
                        .build());
            }
        }
        requirementRepository.saveAll(requirements);

        // --- Labour Cost ---
        double labourCost = request.getLabourForceCount()
                * request.getLabourCostPerPerson()
                * project.getTimeRequiredMonths();

        // --- Time Factor Cost (urgency surcharge) ---
        double timeFactorCost = (project.getTimeRequiredMonths() <= 6) ? materialCost * 0.10 : 0;

        // --- Quality Factor Cost ---
        double qualityFactorCost = (materialCost + labourCost) * (project.getQualityFactor() - 1.0);

        double totalCost = materialCost + labourCost + timeFactorCost + qualityFactorCost;

        CostEstimation estimation = CostEstimation.builder()
                .project(project)
                .materialCost(materialCost)
                .labourCost(labourCost)
                .labourForceCount(request.getLabourForceCount())
                .timeFactorCost(timeFactorCost)
                .qualityFactorCost(qualityFactorCost)
                .totalCost(totalCost)
                .estimatedBy(user)
                .build();

        // Update existing estimation if any
        estimationRepository.findByProjectId(project.getId())
                .ifPresent(e -> estimation.setId(e.getId()));

        return toResponse(estimationRepository.save(estimation));
    }

    @Override
    public CostEstimationDto.Response getByProjectId(Long projectId) {
        CostEstimation e = estimationRepository.findByProjectId(projectId)
                .orElseThrow(() -> new RuntimeException("Estimation not found for project: " + projectId));
        return toResponse(e);
    }

    @Override
    public List<CostEstimationDto.Response> getAllEstimations() {
        return estimationRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<CostEstimationDto.Response> getEstimationsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return estimationRepository.findByEstimatedByUserId(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private CostEstimationDto.Response toResponse(CostEstimation e) {
        return new CostEstimationDto.Response(
                e.getId(), e.getProject().getId(), e.getProject().getName(),
                e.getMaterialCost(), e.getLabourCost(), e.getLabourForceCount(),
                e.getTimeFactorCost(), e.getQualityFactorCost(), e.getProject().getQualityFactor(),
                e.getTotalCost(), e.getEstimatedAt(),
                e.getEstimatedBy() != null ? e.getEstimatedBy().getUsername() : null);
    }
}
