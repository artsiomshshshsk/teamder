package com.github.artsiomshshshsk.findproject.user;

import com.amazonaws.services.opsworks.model.UserProfile;
import com.github.artsiomshshshsk.findproject.application.Application;
import com.github.artsiomshshshsk.findproject.application.ApplicationStatus;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.exception.UnauthorizedAccessException;
import com.github.artsiomshshshsk.findproject.project.Project;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.user.dto.*;
import com.github.artsiomshshshsk.findproject.utils.FileType;
import com.github.artsiomshshshsk.findproject.utils.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final FileUploadService fileUploadService;

    public String uploadFile(User user, MultipartFile file, FileType fileType){

        String fileURL = fileUploadService.uploadFile(file,fileType);

        if (Objects.requireNonNull(fileType) == FileType.CV) {
            if(user.getResumeURL() != null){
                fileUploadService.deleteFile(user.getResumeURL(), FileType.CV);
            }
            user.setResumeURL(fileURL);
        }

        if (Objects.requireNonNull(fileType) == FileType.PROFILE_IMAGE) {
            if(user.getProfilePictureURL() != null){
                fileUploadService.deleteFile(user.getProfilePictureURL(), FileType.PROFILE_IMAGE);
            }
            user.setProfilePictureURL(fileURL);
        }

        userRepository.save(user);
        return fileURL;
    }

    public UserProfileResponse updateLoggedInUser(User user, UserUpdateRequest userUpdateRequest) {

        if(userUpdateRequest.getUsername() != null){
            user.setUsername(userUpdateRequest.getUsername());
        }

        if(userUpdateRequest.getEmail() != null){
            user.setEmail(userUpdateRequest.getUsername());
        }

        if(userUpdateRequest.getPassword() != null){
            user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }

        if(userUpdateRequest.getProfilePicture() != null){
            uploadFile(user, userUpdateRequest.getProfilePicture(), FileType.PROFILE_IMAGE);
        }

        if(userUpdateRequest.getResume() != null){
            uploadFile(user, userUpdateRequest.getResume(), FileType.CV);
        }

        userRepository.save(user);

        return UserProfileResponse.builder()
                .profilePictureURL(user.getProfilePictureURL())
                .resumeUrl(user.getResumeURL())
                .username(user.getUsername())
                .build();

    }


    public Page<DashboardApplicationResponse> getUsersApplications(User user, Pageable pageable) {
        List<Application> applications = user.getApplications();

        List<DashboardApplicationResponse> dashboardApplicationResponses = applications.stream()
                .map(application -> DashboardApplicationResponse.builder()
                        .id(application.getId())
                        .projectName(application.getProject().getName())
                        .applicantMessage(application.getMessage())
                        .resumeURL(application.getResumeURL())
                        .role(application.getRoleRequest().getName())
                        .applicationDate(application.getApplicationDate())
                        .status(application.getStatus())
                        .build())
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dashboardApplicationResponses.size());
        return new PageImpl<>(dashboardApplicationResponses.subList(start, end), pageable, dashboardApplicationResponses.size());

    }

    public Page<DashboardProjectResponse> getUsersProjects(User user, Pageable pageable) {

        List<Project> projects = user.getProjects();

        List<DashboardProjectResponse> dashboardProjectResponses = projects.stream()
                .map(project -> DashboardProjectResponse.builder()
                        .id(project.getId())
                        .name(project.getName())
                        .build())
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dashboardProjectResponses.size());
        return new PageImpl<>(dashboardProjectResponses.subList(start, end), pageable, dashboardProjectResponses.size());
    }

    public ProfileResponse getUserProfile(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found id: " + id));

        List<Participation> participations = new ArrayList<>();

        for (Application application : user.getApplications()) {
            if (application.getStatus().equals(ApplicationStatus.ACCEPTED)) {
                participations.add(Participation.builder()
                        .projectId(application.getProject().getId())
                        .projectTitle(application.getProject().getName())
                        .shortDescription(application.getProject().getShortDescription())
                        .role(application.getRoleRequest().getName())
                        .isOwner(false)
                        .build());
            }
        }

        for (Project project : user.getProjects()) {
            participations.add(Participation.builder()
                    .projectId(project.getId())
                    .projectTitle(project.getName())
                    .shortDescription(project.getShortDescription())
                    .role(project.getRoles().stream()
                            .filter(role -> role.getAssignedUser().getId().equals(user.getId()))
                            .findFirst()
                            .orElseThrow(() -> new UnauthorizedAccessException("User is not owner of the project"))
                            .getName())
                    .isOwner(true)
                    .build());
        }

        return ProfileResponse.builder()
                .avatarUrl(user.getProfilePictureURL())
                .username(user.getUsername())
                .profileSummary(user.getProfileSummary())
                .participations(participations)
                .build();


    }
}
