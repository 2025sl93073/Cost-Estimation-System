package com.ces.controller;

import com.ces.dto.CostEstimationDto;
import com.ces.entity.CostEstimation;
import com.ces.entity.ConstructionProject;
import com.ces.repository.CostEstimationRepository;
import com.ces.repository.ConstructionProjectRepository;
import com.ces.repository.MaterialRepository;
import com.ces.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController {

    @Autowired private CostEstimationRepository estimationRepository;
    @Autowired private ConstructionProjectRepository projectRepository;
    @Autowired private MaterialRepository materialRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalProjects", projectRepository.count());
        summary.put("totalMaterials", materialRepository.count());
        summary.put("totalUsers", userRepository.count());
        summary.put("totalEstimations", estimationRepository.count());
        Double totalCost = estimationRepository.sumTotalCost();
        summary.put("aggregateCost", totalCost != null ? totalCost : 0.0);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/by-city")
    public ResponseEntity<Map<String, Object>> getByCity() {
        List<ConstructionProject> projects = projectRepository.findAll();
        Map<String, Long> byCity = projects.stream()
                .collect(Collectors.groupingBy(ConstructionProject::getCity, Collectors.counting()));
        return ResponseEntity.ok(Collections.singletonMap("byCity", byCity));
    }

    @GetMapping("/by-type")
    public ResponseEntity<Map<String, Object>> getByType() {
        List<ConstructionProject> projects = projectRepository.findAll();
        Map<String, Long> byType = projects.stream()
                .collect(Collectors.groupingBy(p -> p.getConstructionType().name(), Collectors.counting()));
        return ResponseEntity.ok(Collections.singletonMap("byType", byType));
    }

    @GetMapping("/cost-fluctuation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getCostFluctuation() {
        List<CostEstimation> estimations = estimationRepository.findAll();
        List<Map<String, Object>> result = estimations.stream().map(e -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("projectId", e.getProject().getId());
            row.put("projectName", e.getProject().getName());
            row.put("totalCost", e.getTotalCost());
            row.put("materialCost", e.getMaterialCost());
            row.put("labourCost", e.getLabourCost());
            row.put("qualityFactor", e.getProject().getQualityFactor());
            row.put("timeMonths", e.getProject().getTimeRequiredMonths());
            row.put("estimatedAt", e.getEstimatedAt());
            return row;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
