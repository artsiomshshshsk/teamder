package com.github.artsiomshshshsk.findproject.project;


import com.github.artsiomshshshsk.findproject.application.Application;
import com.github.artsiomshshshsk.findproject.application.ApplicationStatus;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationRequest;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectRequest;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.project.dto.CatalogProjectResponse;
import com.github.artsiomshshshsk.findproject.exception.ApplicationCreationException;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.exception.UnauthorizedAccessException;
import com.github.artsiomshshshsk.findproject.application.ApplicationMapper;
import com.github.artsiomshshshsk.findproject.application.ApplicationRepository;
import com.github.artsiomshshshsk.findproject.role.Role;
import com.github.artsiomshshshsk.findproject.utils.FileUploadService;
import com.github.artsiomshshshsk.findproject.user.User;
import com.github.artsiomshshshsk.findproject.utils.FileType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectResponse findProjectById(Long id) {
        return projectMapper.toProjectResponse(findById(id));
    }

    public Project findById(Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Project with id %s not found", id)));
    }

    public static Specification<Project> hasStatus(ProjectStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public Page<CatalogProjectResponse> getProjectCatalog(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(hasStatus(ProjectStatus.RECRUITING),pageable);
        return projects.map(projectMapper::toCatalogProjectResponse);
    }


    public ProjectResponse createProject(User user, ProjectRequest projectRequest) {
        Project project = projectMapper.toProject(user,projectRequest);
        project.setPublishedAt(LocalDateTime.now());
        if(projectRequest.roles().isEmpty()){
            project.setStatus(ProjectStatus.IN_DEVELOPMENT);
        }else{
            project.setStatus(ProjectStatus.RECRUITING);
        }
        user.addProject(project);
        return projectMapper.toProjectResponse(projectRepository.save(project));
    }

}
