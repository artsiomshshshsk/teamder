package com.github.artsiomshshshsk.findproject.user.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;


@Data
@Builder
public class UserUpdateRequest{

        String username;
        String email;
        String password;
        String bio;
        MultipartFile profilePicture;
        MultipartFile resume;
        String contact;
}
