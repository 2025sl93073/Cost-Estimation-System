package com.ces.controller;

import com.ces.dto.MaterialDto;
import com.ces.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materials")
@CrossOrigin(origins = "http://localhost:3000")
public class MaterialController {

    @Autowired private MaterialService materialService;

    @GetMapping
    public ResponseEntity<List<MaterialDto.Response>> getAll() {
        return ResponseEntity.ok(materialService.getAllMaterials());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialDto.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MaterialDto.Response>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(materialService.getMaterialsByCategory(category));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaterialDto.Response> create(@Valid @RequestBody MaterialDto.CreateRequest request) {
        return ResponseEntity.ok(materialService.createMaterial(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaterialDto.Response> update(@PathVariable Long id,
                                                        @Valid @RequestBody MaterialDto.UpdateRequest request) {
        return ResponseEntity.ok(materialService.updateMaterial(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cost")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateCost(@Valid @RequestBody MaterialDto.CostUpdateRequest request,
                                            Authentication authentication) {
        // Retrieve user id from context via username
        materialService.updateMaterialCost(request, null);
        return ResponseEntity.ok().build();
    }
}
