package com.github.artsiomshshshsk.findproject.user;


import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;
import com.github.artsiomshshshsk.findproject.user.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@Api(tags = "User")
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @ApiOperation("Update currently logged in user")
    @PatchMapping
    public ResponseEntity<UserProfileResponse> updateLoggedInUser(
            @ApiIgnore @AuthenticationPrincipal User user,
            @ModelAttribute UserUpdateRequest userUpdateRequest){
        return ResponseEntity.ok(userService.updateLoggedInUser(user,userUpdateRequest));
    }

    @ApiOperation("Get user's projects ")/**/
    @GetMapping("/{userId}/dashboard/projects")

    public ResponseEntity<Page<DashboardProjectResponse>> getUsersProjects(
            @AuthenticationPrincipal @ApiIgnore User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(userService.getUsersProjects(user,pageable));
    }




    @ApiOperation("Get user's applications ")
    @GetMapping("/dashboard/applications")
    public ResponseEntity<Page<DashboardApplicationResponse>> getUsersApplications(
            @ApiIgnore @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(userService.getUsersApplications(user,pageable));
    }


    @ApiOperation("Get currently logged in user's profile")
    @GetMapping
    public ResponseEntity<UserResponse> getUserProfile(
            @ApiIgnore @AuthenticationPrincipal User user
    ){
        return ResponseEntity.ok(userService.getUserProfile(user));
    }


    // TODO: 3.03.23 get all user's applications
}
