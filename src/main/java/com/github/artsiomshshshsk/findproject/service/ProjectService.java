package com.github.artsiomshshshsk.findproject.service;


import com.github.artsiomshshshsk.findproject.domain.Project;
import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.mapper.ProjectMapper;
import com.github.artsiomshshshsk.findproject.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    public Project createProject(User user){
        return Project.builder()
                .publishedAt(LocalDateTime.now())
                .owner(user)
                .build();
    }

    public ProjectResponse findProjectById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if(project.isEmpty()){
            throw new RuntimeException("Project not found");
        }
        return projectMapper.toProjectResponse(project.get());
    }
}
