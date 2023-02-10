package com.github.artsiomshshshsk.findproject.controller;


import com.github.artsiomshshshsk.findproject.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    public ResponseEntity<Void> createProject() {
        return ResponseEntity.ok().build();
    }

}