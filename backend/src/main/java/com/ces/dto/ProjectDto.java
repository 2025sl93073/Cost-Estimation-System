package com.ces.dto;

import com.ces.entity.ConstructionProject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

public class ProjectDto {

    @Data
    public static class CreateRequest {
        @NotBlank
        private String name;
        @NotNull
        private ConstructionProject.ConstructionType constructionType;
        @NotNull
        private ConstructionProject.Type1 type1;
        @NotNull
        private ConstructionProject.Type2 type2;
        @NotNull
        private ConstructionProject.StructureType structureType;
        @NotBlank
        private String area;
        @NotBlank
        private String street;
        @NotBlank
        private String city;
        @NotNull @Positive
        private Double areaSqft;
        @NotNull @Positive
        private Integer timeRequiredMonths;
        @NotNull @Positive
        private Double qualityFactor;
    }

    @Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String constructionType;
        private String type1;
        private String type2;
        private String structureType;
        private String area;
        private String street;
        private String city;
        private Double areaSqft;
        private Integer timeRequiredMonths;
        private Double qualityFactor;
        private String createdBy;
        private LocalDateTime createdAt;
    }
}
