package com.github.artsiomshshshsk.findproject.project;


import com.github.artsiomshshshsk.findproject.user.User;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationRequest;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectRequest;
import com.github.artsiomshshshsk.findproject.project.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.project.dto.CatalogProjectResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @ApiOperation(value = "Find a project by Id")
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> findProjectById(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.findProjectById(projectId));
    }


    @ApiOperation(value = "Get project catalog")
    @GetMapping
    public ResponseEntity<Page<CatalogProjectResponse>> getProjectCatalog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
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
        return ResponseEntity.ok(projectService.createProject(user,projectRequest));
    }

}