package com.github.artsiomshshshsk.findproject.controller;

import com.github.artsiomshshshsk.findproject.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;

import com.github.artsiomshshshsk.findproject.security.config.JwtService;

import com.github.artsiomshshshsk.findproject.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
@Import({JwtService.class})
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Test

    @WithMockUser(username = "user", roles = "USER")
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
    @WithMockUser(username = "user", roles = "USER")
    void givenInvalidId_whenFindProjectById_thenReturnNotFound() throws Exception {
        // given
        Long id = 2L;
        doThrow(new ResourceNotFoundException("Project not found"))
                .when(projectService).findProjectById(id);
        // when + then
        mockMvc.perform(get("/api/projects/{id}", id))
                .andExpect(status().isNotFound());
    }


    @Test

    @WithMockUser(username = "user", roles = "USER")
    void testFindAllProjects() throws Exception {
        // given
        ProjectResponse.builder().build();
        Page<ProjectResponse> projectResponses = new PageImpl<>(Arrays.asList(
                ProjectResponse.builder().build(), ProjectResponse.builder().build())
        );
        given(projectService.findAllProjects(any(Pageable.class))).willReturn(projectResponses);

        // when
        mockMvc.perform(get("/api/projects")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andDo(print());
        // then
        verify(projectService, times(1)).findAllProjects(any(Pageable.class));
    }

}