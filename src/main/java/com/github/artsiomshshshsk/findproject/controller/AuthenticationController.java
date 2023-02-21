package com.github.artsiomshshshsk.findproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.artsiomshshshsk.findproject.security.auth.AuthenticationRequest;
import com.github.artsiomshshshsk.findproject.security.auth.AuthenticationResponse;
import com.github.artsiomshshshsk.findproject.security.auth.AuthenticationService;
import com.github.artsiomshshshsk.findproject.security.auth.RegisterRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "Authentication")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;


  @ApiOperation(value = "Sign-up")
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
          @RequestBody RegisterRequest registerRequest
  ){

    return ResponseEntity.ok(service.register(registerRequest));
  }

  @ApiOperation(value = "Sign-In")
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request ){
    return ResponseEntity.ok(service.authenticate(request));
  }

}
