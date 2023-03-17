package com.github.artsiomshshshsk.findproject.security.auth;


import com.github.artsiomshshshsk.findproject.exception.DuplicateEmailException;
import com.github.artsiomshshshsk.findproject.exception.ResourceNotFoundException;
import com.github.artsiomshshshsk.findproject.notification.service.EmailService;
import com.github.artsiomshshshsk.findproject.security.config.JwtUtils;
import com.github.artsiomshshshsk.findproject.security.dto.AuthenticationRequest;
import com.github.artsiomshshshsk.findproject.security.dto.AuthenticationResponse;
import com.github.artsiomshshshsk.findproject.security.dto.RegisterRequest;
import com.github.artsiomshshshsk.findproject.user.UserMapper;
import com.github.artsiomshshshsk.findproject.user.UserRepository;
import com.github.artsiomshshshsk.findproject.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtils jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserMapper userMapper;

  private final EmailService emailService;


  public AuthenticationResponse register(RegisterRequest registerRequest){
    if(repository.findByEmail(registerRequest.email()).isPresent()){
      throw new DuplicateEmailException("Email is already taken");
    }

    var user = userMapper.toUser(registerRequest);
    user.setPassword(passwordEncoder.encode(registerRequest.password()));
    user.setIsVerified(false);
    user.setVerificationToken(UUID.randomUUID().toString());
    user.setRole(Role.USER);
    try {
      emailService.sendVerificationEmail(user);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
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

  public void verify(String token) {
    var user = repository.findByVerificationToken(token)
        .orElseThrow(
            () -> new ResourceNotFoundException("User not found")
        );
    user.setIsVerified(true);
    user.setVerificationToken(null);
    repository.save(user);
  }
}
