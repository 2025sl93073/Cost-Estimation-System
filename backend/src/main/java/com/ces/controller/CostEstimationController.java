package com.ces.controller;

import com.ces.dto.CostEstimationDto;
import com.ces.service.CostEstimationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estimations")
@CrossOrigin(origins = "http://localhost:3000")
public class CostEstimationController {

    @Autowired private CostEstimationService estimationService;

    @PostMapping
    public ResponseEntity<CostEstimationDto.Response> estimate(
            @Valid @RequestBody CostEstimationDto.EstimateRequest request,
            Authentication auth) {
        return ResponseEntity.ok(estimationService.estimate(request, auth.getName()));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<CostEstimationDto.Response> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(estimationService.getByProjectId(projectId));
    }

    @GetMapping
    public ResponseEntity<List<CostEstimationDto.Response>> getAll() {
        return ResponseEntity.ok(estimationService.getAllEstimations());
    }

    @GetMapping("/my")
    public ResponseEntity<List<CostEstimationDto.Response>> getMy(Authentication auth) {
        return ResponseEntity.ok(estimationService.getEstimationsByUser(auth.getName()));
    }
}
