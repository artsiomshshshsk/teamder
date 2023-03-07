package com.github.artsiomshshshsk.findproject.project;


import com.github.artsiomshshshsk.findproject.application.dto.ApplicationRequest;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;
import com.github.artsiomshshshsk.findproject.project.dto.*;
import com.github.artsiomshshshsk.findproject.user.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.annotations.ApiIgnore;
import javax.validation.Valid;


@Api(tags = "Project")
@RestController
@AllArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private static final String DEFAULT_PAGE_NUMBER = "0";
    private static final String DEFAULT_PAGE_SIZE = "7";
    private static final String DEFAULT_SORT_BY = "id";
    @ApiOperation(value = "Get project Profile")
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectProfileResponse> getProjectProfile(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectProfile(projectId));
    }

    @ApiOperation(value = "Get project catalog")
    @GetMapping
    public ResponseEntity<Page<CatalogProjectResponse>> getProjectCatalog(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(projectService.getProjectCatalog(pageable));
    }

    @ApiOperation(value = "Post project")
    @ApiResponses(value = {
            @ApiResponse( responseCode = "200", description = "Successfully posted a Project"),
            @ApiResponse( responseCode = "401", description = "You are not authorized to post new Project")
    })
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectRequest projectRequest,
            @ApiIgnore @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(projectService.createProject(user,projectRequest), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Apply for the project")
    @PostMapping("/{projectId}/applications")
    public ResponseEntity<ApplicationResponse> createApplication(
            @PathVariable Long projectId,
            @ApiIgnore @AuthenticationPrincipal User user,
            @Valid @ModelAttribute ApplicationRequest applicationRequest
    ) {
        return new ResponseEntity<>(projectService.applyForProject(projectId,user,applicationRequest),HttpStatus.CREATED);
    }


    @ApiOperation(value = "View all applications to the project")
    @GetMapping("/{projectId}/applications")
    public ResponseEntity<Page<ProjectApplicationResponse>> getAllApplications(
            @PathVariable Long projectId,
            @ApiIgnore @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(projectService.getAllApplications(user,projectId,pageable));
    }
}