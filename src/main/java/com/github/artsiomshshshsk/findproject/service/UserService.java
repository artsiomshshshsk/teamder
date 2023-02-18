package com.github.artsiomshshshsk.findproject.service;

import com.github.artsiomshshshsk.findproject.domain.FileType;
import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final FileUploadServiceS3 fileUploadServiceS3;

    public String uploadResume(User user, MultipartFile file, FileType fileType){

        String fileURL = fileUploadServiceS3.uploadFile(file,fileType);

        if (Objects.requireNonNull(fileType) == FileType.CV) {
            user.setResumeURL(fileURL);
        }

        if (Objects.requireNonNull(fileType) == FileType.PROFILE_IMAGE) {
            user.setProfilePictureURL(fileURL);
        }

        userRepository.save(user);
        return fileURL;
    }
}
