package com.ces.service;

import com.ces.dto.ProjectDto;
import java.util.List;

public interface ProjectService {
    List<ProjectDto.Response> getAllProjects();
    ProjectDto.Response getProjectById(Long id);
    ProjectDto.Response createProject(ProjectDto.CreateRequest request, String username);
    ProjectDto.Response updateProject(Long id, ProjectDto.CreateRequest request);
    void deleteProject(Long id);
    List<ProjectDto.Response> getProjectsByUser(String username);
}
