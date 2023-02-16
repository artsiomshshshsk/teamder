package com.github.artsiomshshshsk.findproject.security.auth;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.mapper.UserMapper;
import com.github.artsiomshshshsk.findproject.repository.UserRepository;
import com.github.artsiomshshshsk.findproject.security.Role;
import com.github.artsiomshshshsk.findproject.security.config.JwtService;
import com.github.artsiomshshshsk.findproject.service.FileUploadService;
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
  private final UserMapper userMapper;


  public AuthenticationResponse register(RegisterRequest registerRequest){
    var user = userMapper.toUser(registerRequest);
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
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
