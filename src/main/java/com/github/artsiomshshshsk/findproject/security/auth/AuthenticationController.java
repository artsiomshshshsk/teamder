package com.github.artsiomshshshsk.findproject.security.auth;

import com.github.artsiomshshshsk.findproject.security.dto.AuthenticationRequest;
import com.github.artsiomshshshsk.findproject.security.dto.AuthenticationResponse;
import com.github.artsiomshshshsk.findproject.security.dto.RegisterRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@Api(tags = "Authentication")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @ApiOperation(value = "Sign-up")
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
          @Valid @RequestBody RegisterRequest registerRequest
  ){
    return ResponseEntity.ok(service.register(registerRequest));
  }

  @ApiOperation(value = "Sign-In")
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @Valid @RequestBody AuthenticationRequest request ){
    return ResponseEntity.ok(service.authenticate(request));
  }

}
