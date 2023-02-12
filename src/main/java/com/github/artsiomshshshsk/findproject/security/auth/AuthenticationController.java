package com.github.artsiomshshshsk.findproject.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
          @RequestParam("registerRequest") String registerRequest,
          @RequestParam("resume") MultipartFile file
  ){

    return ResponseEntity.ok(service.register(registerRequest, file));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request ){
    return ResponseEntity.ok(service.authenticate(request));
  }

}
