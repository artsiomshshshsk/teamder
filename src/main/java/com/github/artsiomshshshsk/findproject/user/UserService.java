package com.github.artsiomshshshsk.findproject.user;

import com.github.artsiomshshshsk.findproject.project.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.utils.FileType;
import com.github.artsiomshshshsk.findproject.user.dto.UserProfileResponse;
import com.github.artsiomshshshsk.findproject.user.dto.UserUpdateRequest;
import com.github.artsiomshshshsk.findproject.utils.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
}
