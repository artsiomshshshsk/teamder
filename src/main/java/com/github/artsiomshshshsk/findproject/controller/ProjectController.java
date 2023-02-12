package com.github.artsiomshshshsk.findproject.controller;


import com.amazonaws.services.s3.AmazonS3;
import com.github.artsiomshshshsk.findproject.config.S3ConfigProperties;
import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.dto.ProjectRequest;
import com.github.artsiomshshshsk.findproject.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.io.File;


@RestController
@AllArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> findProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findProjectById(id));
    }


    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> findAllProjects(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(projectService.findAllProjects(pageable));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @RequestBody ProjectRequest projectRequest,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(projectService.createProject(user,projectRequest));
    }
}