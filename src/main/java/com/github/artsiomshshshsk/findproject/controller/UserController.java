package com.github.artsiomshshshsk.findproject.controller;


import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.dto.UserProfileResponse;
import com.github.artsiomshshshsk.findproject.dto.UserResponse;
import com.github.artsiomshshshsk.findproject.dto.UserUpdateRequest;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.github.artsiomshshshsk.findproject.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @ApiOperation("Update currently logged in user")
    @PutMapping
    public ResponseEntity<UserProfileResponse> updateLoggedInUser(
            @ApiIgnore @AuthenticationPrincipal User user,
            @ModelAttribute UserUpdateRequest userUpdateRequest){
        return ResponseEntity.ok(userService.updateLoggedInUser(user,userUpdateRequest));
    }

}
