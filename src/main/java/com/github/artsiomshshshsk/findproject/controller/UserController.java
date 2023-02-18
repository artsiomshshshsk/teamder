package com.github.artsiomshshshsk.findproject.controller;


import com.github.artsiomshshshsk.findproject.domain.FileType;
import com.github.artsiomshshshsk.findproject.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.github.artsiomshshshsk.findproject.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(
            @ApiIgnore @AuthenticationPrincipal User user ,
            @RequestParam MultipartFile file,
            @RequestParam FileType fileType){
        return ResponseEntity.ok(userService.uploadResume(user,file,fileType));
    }
}
