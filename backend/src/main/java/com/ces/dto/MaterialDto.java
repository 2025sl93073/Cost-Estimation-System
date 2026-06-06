package com.ces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;

public class MaterialDto {

    @Data
    public static class CreateRequest {
        @NotBlank
        private String name;
        private String description;
        @NotBlank
        private String unit;
        private String category;
    }

    @Data
    public static class UpdateRequest {
        private String name;
        private String description;
        private String unit;
        private String category;
    }

    @Data
    public static class CostUpdateRequest {
        @NotNull
        private Long materialId;
        @NotNull @Positive
        private Double price;
        private LocalDate effectiveDate;
        private String region;
    }

    @Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private String unit;
        private Double basePricePerUnit;
        private String category;
        private Double latestPrice;
    }
}
