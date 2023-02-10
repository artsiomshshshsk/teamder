package com.github.artsiomshshshsk.findproject.controller;

import com.github.artsiomshshshsk.findproject.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Test
    void givenValidId_whenFindProjectById_thenReturnProjectResponse() throws Exception {
        // given
        Long id = 1L;
        ProjectResponse projectResponse = ProjectResponse.builder()
                .id(id)
                .name("Test Project")
                .description("Test Description")
                .build();
        when(projectService.findProjectById(id)).thenReturn(projectResponse);

        // when + then
        mockMvc.perform(get("/api/projects/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(projectResponse.name()))
                .andExpect(jsonPath("$.description").value(projectResponse.description()));
    }

    @Test
    void givenInvalidId_whenFindProjectById_thenReturnNotFound() throws Exception {
        // given
        Long id = 2L;
        doThrow(new ResourceNotFoundException("Project not found"))
                .when(projectService).findProjectById(id);
        // when + then
        mockMvc.perform(get("/api/projects/{id}", id))
                .andExpect(status().isNotFound());
    }




}