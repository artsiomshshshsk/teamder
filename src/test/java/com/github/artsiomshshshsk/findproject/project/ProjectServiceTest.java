package com.github.artsiomshshshsk.findproject.project;

import com.github.artsiomshshshsk.findproject.project.dto.ProjectProfileResponse;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.project.dto.CatalogProjectResponse;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Test
    void givenValidId_whenFindProjectById_thenReturnProjectResponse() {
        // given
        Long id = 1L;
        Project project = Project.builder()
                .id(id)
                .name("Test Project")
                .build();
        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        ProjectResponse expectedResponse = ProjectResponse.builder()
                .id(id)
                .name("Test Project")
                .build();
        when(projectMapper.toProjectResponse(project)).thenReturn(expectedResponse);
        // when
        ProjectProfileResponse projectResponse = projectService.findProjectById(id);
        // then
        assertEquals(expectedResponse, projectResponse);
    }

    @Test
    void givenInvalidId_whenFindProjectById_thenThrowResourceNotFoundException() {
        // given
        Long id = 2L;
        when(projectRepository.findById(id)).thenReturn(Optional.empty());
        // when + then
        assertThrows(ResourceNotFoundException.class, () -> projectService.findProjectById(id));
    }


    @Test
    void givenPageable_whenFindAllProjects_thenReturnPageOfProjectResponse() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<Project> projects = Arrays.asList(
                Project.builder().id(1L).name("Project 1").isVisible(true).build(),
                Project.builder().id(2L).name("Project 2").isVisible(true).build()
        );
        Page<Project> projectPage = new PageImpl<>(projects, pageable, 2);


        when(projectRepository.findAllByIsVisibleTrue(
                ArgumentMatchers.any(Pageable.class))
        ).thenReturn(projectPage);

        CatalogProjectResponse projectResponse1 = CatalogProjectResponse.builder().id(1L).name("Project 1").build();
        CatalogProjectResponse projectResponse2 = CatalogProjectResponse.builder().id(2L).name("Project 2").build();
        when(projectMapper.toCatalogProjectResponse(projects.get(0))).thenReturn(projectResponse1);
        when(projectMapper.toCatalogProjectResponse(projects.get(1))).thenReturn(projectResponse2);

        // when
        Page<CatalogProjectResponse> projectResponsePage = projectService.getProjectCatalog(pageable);

        // then
        assertThat(projectResponsePage.getTotalPages()).isEqualTo(1);
        assertThat(projectResponsePage.getContent()).containsExactly(projectResponse1, projectResponse2);
    }

}