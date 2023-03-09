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
        ProjectProfileResponse expectedResponse = ProjectProfileResponse.builder()
                .id(id)
                .name("Test Project")
                .build();
        when(projectMapper.toProjectProfileResponse(project)).thenReturn(expectedResponse);
        // when
        ProjectProfileResponse projectResponse = projectService.getProjectProfile(id);
        // then
        assertEquals(expectedResponse, projectResponse);
    }

    @Test
    void givenInvalidId_whenFindProjectById_thenThrowResourceNotFoundException() {
        // given
        Long id = 2L;
        when(projectRepository.findById(id)).thenReturn(Optional.empty());
        // when + then
        assertThrows(ResourceNotFoundException.class, () -> projectService.getProjectProfile(id));
    }

}