package com.github.artsiomshshshsk.findproject.user;


import com.github.artsiomshshshsk.findproject.user.dto.UserProfileResponse;
import com.github.artsiomshshshsk.findproject.user.dto.UserUpdateRequest;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
