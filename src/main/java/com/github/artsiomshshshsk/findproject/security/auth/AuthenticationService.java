package com.github.artsiomshshshsk.findproject.security.auth;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.repository.UserRepository;
import com.github.artsiomshshshsk.findproject.security.Role;
import com.github.artsiomshshshsk.findproject.security.config.JwtService;
import com.github.artsiomshshshsk.findproject.service.FileUploadService;
import com.github.artsiomshshshsk.findproject.service.FileUploadServiceS3;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final FileUploadService fileUploadService;

  private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

  public AuthenticationResponse register(String registerRequest, MultipartFile fileResume){

    RegisterRequest request = null;
    try {
      request = new ObjectMapper().readValue(registerRequest, RegisterRequest.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    String fileName = fileUploadService.uploadFile(fileResume,"application/pdf");
    var user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .cvFilename(fileName)
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request  ) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow(
            () -> new RuntimeException("User not found")
        );
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }
}
