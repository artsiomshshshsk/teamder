package com.github.artsiomshshshsk.findproject.user;

import com.github.artsiomshshshsk.findproject.application.Application;
import com.github.artsiomshshshsk.findproject.application.ApplicationRepository;
import com.github.artsiomshshshsk.findproject.application.ApplicationStatus;
import com.github.artsiomshshshsk.findproject.exception.DuplicateResourceException;
import com.github.artsiomshshshsk.findproject.exception.IllegalFileFormat;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.exception.UnauthorizedAccessException;
import com.github.artsiomshshshsk.findproject.project.Project;
import com.github.artsiomshshshsk.findproject.project.ProjectRepository;
import com.github.artsiomshshshsk.findproject.user.dto.*;
import com.github.artsiomshshshsk.findproject.utils.UploadType;
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
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationRepository applicationRepository;

    private final ProjectRepository projectRepository;

    private final FileUploadService fileUploadService;

    private final static Set<String> allowedImageTypes = Set.of("image/png", "image/jpeg", "image/jpg", "image/svg+xml");

    private final static String PDF_TYPE = "application/pdf";

    public String uploadFile(User user, MultipartFile file, UploadType uploadType){
        if(uploadType == UploadType.PROFILE_IMAGE){
            return uploadProfileImage(user, file);
        }else if(uploadType == UploadType.CV){
            return uploadCV(user, file);
        }else{
            throw new IllegalArgumentException("Upload type is not supported: " + uploadType);
        }
    }

    private String uploadProfileImage(User user, MultipartFile file){
        if(!allowedImageTypes.contains(file.getContentType())){
            throw new IllegalFileFormat("File type is not supported:" + file.getContentType() + ". Choose one of the following: " + allowedImageTypes);
        }
        String fileURL = fileUploadService.uploadFile(file);
        if(user.getProfilePictureURL() != null){
            fileUploadService.deleteFile(user.getProfilePictureURL());
        }
        user.setProfilePictureURL(fileURL);
        userRepository.save(user);
        return fileURL;
    }

    private String uploadCV(User user, MultipartFile file){
        if(file.getContentType() != PDF_TYPE){
            throw new IllegalFileFormat("File type is not supported:" + file.getContentType() + ". Choose the following: " + PDF_TYPE);
        }
        String fileURL = fileUploadService.uploadFile(file);
        if(user.getResumeURL() != null){
            fileUploadService.deleteFile(user.getResumeURL());
        }
        user.setResumeURL(fileURL);
        userRepository.save(user);
        return fileURL;
    }

    public ProfileResponse updateLoggedInUser(User user, Long id,UserUpdateRequest userUpdateRequest) {

        if(!user.getId().equals(id)){
            throw new UnauthorizedAccessException("You are not authorized to update this user");
        }

        if(userUpdateRequest.getUsername() != null){
            if(userUpdateRequest.getUsername().length() > 15){
                throw new IllegalArgumentException("Username must be less than 15 characters");
            }
            if(userRepository.findByUsername(userUpdateRequest.getUsername()).isPresent()){
                throw new DuplicateResourceException(userUpdateRequest.getUsername() + " is taken by another user");
            }
            user.setUsername(userUpdateRequest.getUsername());
        }

        if(userUpdateRequest.getEmail() != null){
            // check if email is valid
            String regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
            if(!userUpdateRequest.getEmail().matches(regexp)){
                throw new IllegalArgumentException("Email is not valid");
            }

            if(userRepository.findByEmail(userUpdateRequest.getEmail()).isPresent()){
                throw new DuplicateResourceException(userUpdateRequest.getEmail() + " is taken by another user");
            }
            user.setEmail(userUpdateRequest.getEmail());
        }

        if(userUpdateRequest.getPassword() != null){
            if(userUpdateRequest.getPassword().length() < 10){
                throw new IllegalArgumentException("Password must be at least 10 characters");
            }
            user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }

        if(userUpdateRequest.getContact() != null){
            user.setContact(userUpdateRequest.getContact());
        }

        if(userUpdateRequest.getBio() != null){
            user.setBio(userUpdateRequest.getBio());
        }

        if(userUpdateRequest.getProfilePicture() != null){
            if(user.getProfilePictureURL() != null){
                fileUploadService.deleteFile(user.getProfilePictureURL());
            }
            uploadFile(user, userUpdateRequest.getProfilePicture(), UploadType.PROFILE_IMAGE);
        }

        if(userUpdateRequest.getResume() != null){
            if(user.getResumeURL() != null){
                fileUploadService.deleteFile(user.getResumeURL());
            }
            uploadFile(user, userUpdateRequest.getResume(), UploadType.CV);
        }

        userRepository.save(user);

        return getUserProfile(user.getId());
    }


    public Page<DashboardApplicationResponse> getUsersApplications(User user, Pageable pageable) {
        List<Application> applications = user.getApplications();

        List<DashboardApplicationResponse> dashboardApplicationResponses = applications.stream()
                .map(userMapper::toDashboardApplicationResponse)
                .toList();

        return listToPage(pageable, dashboardApplicationResponses);

    }

    private static <T> PageImpl<T> listToPage(Pageable pageable, List<T> list) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    public Page<DashboardProjectResponse> getUsersProjects(User user, Pageable pageable) {

        List<Project> projects = user.getProjects();

        List<DashboardProjectResponse> dashboardProjectResponses = projects.stream()
                .map(project -> DashboardProjectResponse.builder()
                        .id(project.getId())
                        .name(project.getName())
                        .build())
                .toList();

        return listToPage(pageable, dashboardProjectResponses);
    }

    public ProfileResponse getUserProfile(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found id: " + id));

        List<Participation> participations = new ArrayList<>();


        for (Application application : applicationRepository.findAllByApplicantId(id)) {
            if (application.getStatus().equals(ApplicationStatus.ACCEPTED)) {
                participations.add(userMapper.toParticipation(application));
            }
        }

        for (Project project : projectRepository.findAllByOwnerId(id)) {
            participations.add(userMapper.toParticipation(project));
        }

        return ProfileResponse.builder()
                .id(user.getId())
                .avatarUrl(user.getProfilePictureURL())
                .username(user.getUsername())
                .resumeUrl(user.getResumeURL())
                .bio(user.getBio())
                .participations(participations)
                .build();

    }

    public CurrentUserProfile getLoggedInUserProfile(User user) {
        return CurrentUserProfile.builder()
                .username(user.getUsername())
                .id(user.getId())
                .avatarUrl(user.getProfilePictureURL())
                .build();
    }
}
