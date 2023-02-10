package com.github.artsiomshshshsk.findproject.service;

import com.github.artsiomshshshsk.findproject.domain.Project;
import com.github.artsiomshshshsk.findproject.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.mapper.ProjectMapper;
import com.github.artsiomshshshsk.findproject.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        ProjectResponse projectResponse = projectService.findProjectById(id);
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

}