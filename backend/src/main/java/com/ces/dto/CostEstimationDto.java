package com.ces.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

public class CostEstimationDto {

    @Data
    public static class MaterialItem {
        @NotNull
        private Long materialId;
        @NotNull @Positive
        private Double quantity;
    }

    @Data
    public static class EstimateRequest {
        @NotNull
        private Long projectId;
        @NotNull @Positive
        private Integer labourForceCount;
        @NotNull @Positive
        private Double labourCostPerPerson;
        private List<MaterialItem> materials;
    }

    @Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class Response {
        private Long id;
        private Long projectId;
        private String projectName;
        private Double materialCost;
        private Double labourCost;
        private Integer labourForceCount;
        private Double qualityFactorCost;
        private Double qualityFactor;
        private Double totalCost;
        private LocalDateTime estimatedAt;
        private String estimatedBy;
    }
}
