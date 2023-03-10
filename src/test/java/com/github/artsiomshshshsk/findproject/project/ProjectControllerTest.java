package com.github.artsiomshshshsk.findproject.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectRequest;
import com.github.artsiomshshshsk.findproject.role.dto.RoleRequest;
import com.github.artsiomshshshsk.findproject.security.Role;
import com.github.artsiomshshshsk.findproject.security.config.JwtService;
import com.github.artsiomshshshsk.findproject.user.User;
import com.github.artsiomshshshsk.findproject.user.UserRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql(scripts = "classpath:db/clear-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Project project;

    @Autowired
    private ProjectRepository projectRepository;

    private User projectOwner;

    private User applicant;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;
    private String projectOwnerToken;
    private String applicantToken;


    @BeforeEach
    void setUp(){

        projectOwner = User.builder()
                .username("projectOwner")
                .role(Role.USER)
                .email("projectOwner@gmail.com")
                .password(passwordEncoder.encode("password"))
                .build();
        projectOwner = userRepository.save(projectOwner);
        projectOwnerToken = jwtService.generateToken(projectOwner);


        applicant = User.builder()
                .username("applicant")
                .role(Role.USER)
                .password(passwordEncoder.encode("password"))
                .email("applicant@email.com")
                .build();
        applicant = userRepository.save(applicant);
        applicantToken = jwtService.generateToken(applicant);

        project = Project.builder()
                .name("Test Project")
                .shortDescription("Test project short description")
                .description("Test project description")
                .roles(
                        List.of(com.github.artsiomshshshsk.findproject.role.Role.builder()
                                        .name("Test Role")
                                        .assignedUser(null)
                                        .build(),
                                com.github.artsiomshshshsk.findproject.role.Role.builder()
                                        .name("Owner role")
                                        .assignedUser(projectOwner)
                                        .build()
                                )
                )
                .chatInviteLink("https://test-chat-link.com")
                .publishedAt(LocalDateTime.now())
                .owner(projectOwner)
                .isVisible(true)
                .build();
        projectRepository.save(project);
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


    @Test
    void findProjectByIdIsFound() throws Exception {
        Long id = project.getId();

        mockMvc.perform(get("/api/projects/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(project.getName()))
                .andExpect(jsonPath("$.shortDescription").value(project.getShortDescription()))
                .andExpect(jsonPath("$.description").value(project.getDescription()))
                .andExpect(jsonPath("$.openedRoles.length()").value(project.getRoles().size() - 1))
                .andExpect(jsonPath("$.ownerId").value(project.getOwner().getId()))
                .andExpect(jsonPath("$.publishedAt").isNotEmpty());
    }


}