package com.github.artsiomshshshsk.findproject.service;


import com.github.artsiomshshshsk.findproject.domain.Project;
import com.github.artsiomshshshsk.findproject.domain.ProjectStatus;
import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.dto.ProjectRequest;
import com.github.artsiomshshshsk.findproject.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.mapper.ProjectMapper;
import com.github.artsiomshshshsk.findproject.repository.ProjectRepository;
import com.github.artsiomshshshsk.findproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    public ProjectResponse findProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Project with id %s not found", id)));
        return projectMapper.toProjectResponse(project);
    }

    public Page<ProjectResponse> findAllProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);
        return projects.map(projectMapper::toProjectResponse);
    }


    public ProjectResponse createProject(User user, ProjectRequest projectRequest) {
        Project project = projectMapper.toProject(user,projectRequest);
        project.setPublishedAt(LocalDateTime.now());
        if(projectRequest.roles().isEmpty()){
            project.setStatus(ProjectStatus.IN_DEVELOPMENT);
        }else{
            project.setStatus(ProjectStatus.RECRUITING);
        }
        Project savedProject = projectRepository.saveAndFlush(project);
        user.addProject(savedProject);
        userRepository.saveAndFlush(user);
        return projectMapper.toProjectResponse(project);
    }
}
