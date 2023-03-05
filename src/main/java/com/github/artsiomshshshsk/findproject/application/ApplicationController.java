package com.github.artsiomshshshsk.findproject.application;

import com.github.artsiomshshshsk.findproject.application.dto.ApplicationResponse;
import com.github.artsiomshshshsk.findproject.application.dto.UpdateApplicationRequest;
import com.github.artsiomshshshsk.findproject.user.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.validation.Valid;

@Api(tags = "Applications")
@RestController
@AllArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @ApiOperation(value = "Process application decision")
    @PutMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse> processApplicationDecision(
            @PathVariable Long applicationId,
            @ApiIgnore @AuthenticationPrincipal User user,
            @RequestParam ApplicationDecision decision
    ){
        return ResponseEntity.ok(applicationService.processApplicationDecision(user,applicationId,decision));
    }

    @ApiOperation(value = "Update applications for project")
    @PatchMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse> updateApplication(
            @PathVariable Long applicationId,
            @ApiIgnore @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateApplicationRequest updateApplicationRequest
    ){
        return ResponseEntity.ok(applicationService.updateApplication(user,applicationId,updateApplicationRequest));
    }

}
