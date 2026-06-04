package com.ces.service.impl;

import com.ces.dto.ProjectDto;
import com.ces.entity.ConstructionProject;
import com.ces.entity.User;
import com.ces.repository.ConstructionProjectRepository;
import com.ces.repository.UserRepository;
import com.ces.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired private ConstructionProjectRepository projectRepository;
    @Autowired private UserRepository userRepository;

    @Override
    public List<ProjectDto.Response> getAllProjects() {
        return projectRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ProjectDto.Response getProjectById(Long id) {
        ConstructionProject p = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found: " + id));
        return toResponse(p);
    }

    @Override
    public ProjectDto.Response createProject(ProjectDto.CreateRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ConstructionProject project = ConstructionProject.builder()
                .name(request.getName())
                .constructionType(request.getConstructionType())
                .type1(request.getType1())
                .type2(request.getType2())
                .structureType(request.getStructureType())
                .area(request.getArea())
                .street(request.getStreet())
                .city(request.getCity())
                .areaSqft(request.getAreaSqft())
                .timeRequiredMonths(request.getTimeRequiredMonths())
                .qualityFactor(request.getQualityFactor())
                .createdBy(user)
                .build();
        return toResponse(projectRepository.save(project));
    }

    @Override
    public ProjectDto.Response updateProject(Long id, ProjectDto.CreateRequest request) {
        ConstructionProject p = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found: " + id));
        p.setName(request.getName());
        p.setConstructionType(request.getConstructionType());
        p.setType1(request.getType1());
        p.setType2(request.getType2());
        p.setStructureType(request.getStructureType());
        p.setArea(request.getArea());
        p.setStreet(request.getStreet());
        p.setCity(request.getCity());
        p.setAreaSqft(request.getAreaSqft());
        p.setTimeRequiredMonths(request.getTimeRequiredMonths());
        p.setQualityFactor(request.getQualityFactor());
        return toResponse(projectRepository.save(p));
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public List<ProjectDto.Response> getProjectsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return projectRepository.findByCreatedById(user.getId()).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    private ProjectDto.Response toResponse(ConstructionProject p) {
        return new ProjectDto.Response(
                p.getId(), p.getName(),
                p.getConstructionType().name(), p.getType1().name(),
                p.getType2().name(), p.getStructureType().name(),
                p.getArea(), p.getStreet(), p.getCity(),
                p.getAreaSqft(), p.getTimeRequiredMonths(), p.getQualityFactor(),
                p.getCreatedBy().getUsername(), p.getCreatedAt());
    }
}
