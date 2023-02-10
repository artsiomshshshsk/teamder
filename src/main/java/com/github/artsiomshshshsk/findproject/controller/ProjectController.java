package com.github.artsiomshshshsk.findproject.controller;


import com.github.artsiomshshshsk.findproject.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;


    @GetMapping("/{id}")
    public ProjectResponse findProjectById(Long id) {
        return projectService.findProjectById(id);
    }

    public ResponseEntity<Void> createProject() {
        return ResponseEntity.ok().build();
    }

}