package com.github.artsiomshshshsk.findproject.controller;


import com.github.artsiomshshshsk.findproject.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.github.artsiomshshshsk.findproject.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/resume")
    public ResponseEntity<String> uploadResume(
            @ApiIgnore @AuthenticationPrincipal User user ,
            @PathVariable MultipartFile resume){
        System.out.println(user);
        return ResponseEntity.ok(userService.uploadResume(user,resume));
    }
}
