package com.github.artsiomshshshsk.findproject.application;

import com.github.artsiomshshshsk.findproject.application.dto.ApplicationRequest;
import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;
import com.github.artsiomshshshsk.findproject.user.User;
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
import javax.validation.Valid;

@Api(tags = "Application to Project")
@RestController
@AllArgsConstructor
@RequestMapping("/api/projects")
public class ApplicationController {

    private final ApplicationService applicationService;

    @ApiOperation(
            value = "Apply for the project"
    )

    @PostMapping("/{projectId}/application")
    public ResponseEntity<ApplicationResponse> createApplication(
            @PathVariable Long projectId,
            @ApiIgnore @AuthenticationPrincipal User user,
            @Valid @ModelAttribute ApplicationRequest applicationRequest
    ) {
        return ResponseEntity.ok(applicationService.createApplication(applicationRequest,user,projectId));
    }

    @ApiOperation(value = "View all applications to the project")
    @GetMapping("/{projectId}/application")
    public ResponseEntity<Page<ApplicationResponse>> getAllApplications(
            @PathVariable Long projectId,
            @ApiIgnore @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(applicationService.getAllApplications(user,projectId,pageable));
    }

    @ApiOperation(value = "Process application decision")
    @PutMapping("/{projectId}/application/{applicationId}")
    public ResponseEntity<ApplicationResponse> processApplicationDecision(
            @PathVariable Long projectId,
            @PathVariable Long applicationId,
            @ApiIgnore @AuthenticationPrincipal User user,
            @RequestParam ApplicationDecision decision
    ){
        return ResponseEntity.ok(applicationService.processApplicationDecision(user,projectId,applicationId,decision));
    }
}
