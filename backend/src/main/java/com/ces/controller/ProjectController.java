package com.ces.controller;

import com.ces.dto.ProjectDto;
import com.ces.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {

    @Autowired private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectDto.Response>> getAll() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ProjectDto.Response>> getMyProjects(Authentication auth) {
        return ResponseEntity.ok(projectService.getProjectsByUser(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<ProjectDto.Response> create(@Valid @RequestBody ProjectDto.CreateRequest request,
                                                       Authentication auth) {
        return ResponseEntity.ok(projectService.createProject(request, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto.Response> update(@PathVariable Long id,
                                                       @Valid @RequestBody ProjectDto.CreateRequest request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
