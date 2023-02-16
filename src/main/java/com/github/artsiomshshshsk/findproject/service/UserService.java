package com.github.artsiomshshshsk.findproject.service;

import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final FileUploadServiceS3 fileUploadServiceS3;

    public String uploadResume(User user, MultipartFile file){
        String resumeURL = fileUploadServiceS3.uploadFile(file,"application/pdf");
        user.setResumeURL(resumeURL);
        userRepository.saveAndFlush(user);
        return resumeURL;
    }
}
