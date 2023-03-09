package com.github.artsiomshshshsk.findproject.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectProfileResponse;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectRequest;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.project.dto.CatalogProjectResponse;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.role.dto.RoleRequest;
import com.github.artsiomshshshsk.findproject.security.Role;
import com.github.artsiomshshshsk.findproject.security.config.JwtService;
import com.github.artsiomshshshsk.findproject.user.User;
import com.github.artsiomshshshsk.findproject.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql(scripts = "classpath:db/clear-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Project project;

    private ProjectRepository projectRepository;

    private User projectOwner;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;
    private String projectOwnerToken;




    @BeforeEach
    void setUp(){

        projectOwner = User.builder()
                .username("projectOwner")
                .role(Role.USER)
                .password("password")
                .email("projectOwner@gmail.com")
                .password(passwordEncoder.encode("password"))
                .build();
        projectOwner = userRepository.save(projectOwner);


        projectOwnerToken = jwtService.generateToken(projectOwner);


    }

    @Test
    void createProjectSuccess() throws Exception {


        ProjectRequest projectRequest = ProjectRequest.builder()
                .name("Test Project")
                .shortDescription("Test project short description")
                .description("Test project description")
                .roles(Arrays.asList(RoleRequest.builder().name("Test Role").build()))
                .chatInviteLink("https://test-chat-link.com")
                .ownerRole(RoleRequest.builder().name("Owner").build())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();


        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + projectOwnerToken)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(projectRequest.name()))
                .andExpect(jsonPath("$.shortDescription").value(projectRequest.shortDescription()))
                .andExpect(jsonPath("$.description").value(projectRequest.description()))
                .andExpect(jsonPath("$.roles.length()").value(projectRequest.roles().size() + 1))
                .andExpect(jsonPath("$.owner.id").value(projectOwner.getId()))
                .andExpect(jsonPath("$.publishedAt").isNotEmpty());
    }


    @Test
    void createProjectWithBlankName() throws Exception {

        ProjectRequest projectRequest = ProjectRequest.builder()
                .shortDescription("Test project short description")
                .description("Test project description")
                .roles(Arrays.asList(RoleRequest.builder().name("Test Role").build()))
                .chatInviteLink("https://test-chat-link.com")
                .ownerRole(RoleRequest.builder().name("Owner").build())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + projectOwnerToken)
                        .content(objectMapper.writeValueAsString(projectRequest)))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Project name is mandatory"));
    }








}