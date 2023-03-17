package com.github.artsiomshshshsk.findproject.application;

import com.github.artsiomshshshsk.findproject.project.Project;
import com.github.artsiomshshshsk.findproject.project.ProjectRepository;
import com.github.artsiomshshshsk.findproject.security.Role;
import com.github.artsiomshshshsk.findproject.security.config.JwtUtils;
import com.github.artsiomshshshsk.findproject.user.User;
import com.github.artsiomshshshsk.findproject.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Sql(scripts = "classpath:db/clear-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureMockMvc
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProjectRepository projectRepository;

    private User projectOwner;
    private String projectOwnerToken;

    private User applicant;
    private String applicantToken;

    private com.github.artsiomshshshsk.findproject.role.Role role;

    private Project project;

    private Application application;

    @Autowired
    private JwtUtils jwtService;

    @BeforeEach
    void setUp() {
        projectOwner = User.builder()
                .username("projectOwner")
                .role(Role.USER)
                .password("password")
                .email("projectOwner@gmail.com")
                .password(passwordEncoder.encode("password"))
                .build();
        userRepository.save(projectOwner);

        projectOwnerToken = jwtService.generateToken(projectOwner);


        applicant = User.builder()
                .username("applicant")
                .role(Role.USER)
                .password(passwordEncoder.encode("password"))
                .email("applicant@gmail.com")
                .build();
        userRepository.save(applicant);

        applicantToken = jwtService.generateToken(applicant);


        role = com.github.artsiomshshshsk.findproject.role.Role.builder()
                .name("Backend developer")
                .assignedUser(null)
                .build();


        project = Project.builder()
                .owner(projectOwner)
                .roles(List.of(role))
                .build();

        project = projectRepository.save(project);

        application = Application.builder()
                .applicant(applicant)
                .status(ApplicationStatus.WAITING_FOR_REVIEW)
                .roleRequest(role)
                .project(project)
                .build();
        application = applicationRepository.save(application);
    }



    @Test
    void testProcessApplicationDecision() throws Exception {
        mockMvc.perform(put("/api/applications/{applicationId}", application.getId())
                        .header("Authorization", "Bearer " + projectOwnerToken)
                .param("decision", "ACCEPTED"))
                .andExpect(status().isOk());

        Application processedApplication = applicationRepository.findById(application.getId()).get();
        assertThat(processedApplication.getStatus()).isEqualTo(ApplicationStatus.ACCEPTED);
    }

    @Test
    void testProcessApplicationDecisionRejected() throws Exception {

        mockMvc.perform(put("/api/applications/{applicationId}", application.getId())
                        .header("Authorization", "Bearer " + projectOwnerToken)
                .param("decision", ApplicationDecision.REJECTED.toString()))
                .andExpect(status().isOk());

        Application processedApplication = applicationRepository.findById(application.getId()).get();
        assertThat(processedApplication.getStatus()).isEqualTo(ApplicationStatus.REJECTED);
    }

    @Test
    void testProcessApplicationDecisionWithWrongUser() throws Exception {

        mockMvc.perform(put("/api/applications/{applicationId}", application.getId())
                        .header("Authorization", "Bearer " + applicantToken)
                .param("decision", ApplicationDecision.ACCEPTED.toString()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You can't process application decision for other user's project"));
    }

    @Test
    void testProcessApplicationDecisionWithWrongStatus() throws Exception {

        application.setStatus(ApplicationStatus.ACCEPTED);
        applicationRepository.saveAndFlush(application);

        mockMvc.perform(put("/api/applications/{applicationId}", application.getId())
                        .header("Authorization", "Bearer " + projectOwnerToken)
                .param("decision", ApplicationDecision.ACCEPTED.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("You can't change the status of t" +
                        "he application that is not in waiting for review status"));
    }

    @Test
    void testProcessApplicationDecisionWithOccupiedRole() throws Exception {
        role.setAssignedUser(applicant);
        projectRepository.save(project);

        mockMvc.perform(put("/api/applications/{applicationId}", application.getId())
                        .header("Authorization", "Bearer " + projectOwnerToken)
                .param("decision", ApplicationDecision.ACCEPTED.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("You can't accept application for role: " +
                        "%s because it is occupied".formatted(role.getName())));
        assertTrue(application.getStatus().equals(ApplicationStatus.WAITING_FOR_REVIEW));
    }


    @Test
    void testRemoveApplicatiom() throws Exception {
        mockMvc.perform(delete("/api/applications/{applicationId}", application.getId())
                        .header("Authorization", "Bearer " + applicantToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRemoveApplicationOtherOwner() throws Exception {
        mockMvc.perform(delete("/api/applications/{applicationId}", application.getId())
                        .header("Authorization", "Bearer " + projectOwnerToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You can't remove application for other user"));
    }

    @Test
    void testRemoveApplicationIllegalStatus() throws Exception {
        application.setStatus(ApplicationStatus.ACCEPTED);
        applicationRepository.saveAndFlush(application);

        mockMvc.perform(delete("/api/applications/{applicationId}", application.getId())
                        .header("Authorization", "Bearer " + applicantToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("You can't remove the application " +
                        "that is not in waiting for review status"));

    }


}