package com.github.artsiomshshshsk.findproject.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
@Builder
public class UserUpdateRequest{
        String username;
        String email;
        String password;
        MultipartFile profilePicture;
        MultipartFile resume;
        String contact;
}
