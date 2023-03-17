package com.github.artsiomshshshsk.findproject.security.auth;

import com.github.artsiomshshshsk.findproject.security.dto.AuthenticationRequest;
import com.github.artsiomshshshsk.findproject.security.dto.AuthenticationResponse;
import com.github.artsiomshshshsk.findproject.security.dto.RegisterRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
  @ApiResponses(value = {
          @ApiResponse( responseCode = "200", description = "Successfully authenticated"),
          @ApiResponse( responseCode = "401", description = "Bad credentials")
  })
  public ResponseEntity<AuthenticationResponse> authenticate(
      @Valid @RequestBody AuthenticationRequest request ){
    return ResponseEntity.ok(service.authenticate(request));
  }


  @GetMapping("/verify")
  public ResponseEntity<Void> verifyToken(@RequestParam String verificationToken){
    service.verify(verificationToken);
    return ResponseEntity.ok().build();
  }

}
