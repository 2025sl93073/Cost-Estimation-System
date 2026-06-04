package com.ces.service;

import com.ces.dto.CostEstimationDto;
import java.util.List;

public interface CostEstimationService {
    CostEstimationDto.Response estimate(CostEstimationDto.EstimateRequest request, String username);
    CostEstimationDto.Response getByProjectId(Long projectId);
    List<CostEstimationDto.Response> getAllEstimations();
    List<CostEstimationDto.Response> getEstimationsByUser(String username);
}
