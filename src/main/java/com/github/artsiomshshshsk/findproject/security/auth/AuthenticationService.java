package com.github.artsiomshshshsk.findproject.security.auth;


import com.github.artsiomshshshsk.findproject.exception.DuplicateEmailException;
import com.github.artsiomshshshsk.findproject.user.UserMapper;
import com.github.artsiomshshshsk.findproject.user.UserRepository;
import com.github.artsiomshshshsk.findproject.security.Role;
import com.github.artsiomshshshsk.findproject.security.config.JwtService;
import com.github.artsiomshshshsk.findproject.utils.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserMapper userMapper;

  private final FileUploadService fileUploadService;


  public AuthenticationResponse register(RegisterRequest registerRequest){
    if(repository.findByEmail(registerRequest.email()).isPresent()){
      throw new DuplicateEmailException("Email is already taken");
    }

    var user = userMapper.toUser(registerRequest);
    user.setPassword(passwordEncoder.encode(registerRequest.password()));
    user.setRole(Role.USER);
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
