package com.github.artsiomshshshsk.findproject.security.auth;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.artsiomshshshsk.findproject.config.S3ConfigProperties;
import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.repository.UserRepository;
import com.github.artsiomshshshsk.findproject.security.Role;
import com.github.artsiomshshshsk.findproject.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AmazonS3 s3Client;
  private final S3ConfigProperties s3ConfigProperties;
  private final AuthenticationManager authenticationManager;

  private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

  public AuthenticationResponse register(String registerRequest, MultipartFile fileResume) {

    // Use a unique filename
    String filename = UUID.randomUUID().toString() + ".pdf";
    while(s3Client.doesObjectExist(s3ConfigProperties.bucketName(), filename)){
        filename = UUID.randomUUID().toString() + ".pdf";
    }
    RegisterRequest request;
    try {
      request = new ObjectMapper().readValue(registerRequest, RegisterRequest.class);
      // Validate the uploaded file
      if (fileResume.isEmpty() || !fileResume.getContentType().equals("application/pdf")) {
        throw new RuntimeException("Invalid file");
      }

      // Store the file temporarily
      Path tempFile = Files.createTempFile(Paths.get(TEMP_DIR), "temp-", ".pdf");
      Files.write(tempFile, fileResume.getBytes());


      // Upload the file to S3
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType("application/pdf");
      PutObjectRequest putObjectRequest = new PutObjectRequest(s3ConfigProperties.bucketName(), filename, tempFile.toFile());
      putObjectRequest.setMetadata(metadata);
      s3Client.putObject(putObjectRequest);

      // Delete the temporary file
      Files.delete(tempFile);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }


    var user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .cvFilename(s3ConfigProperties.endpoint() + filename)
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
