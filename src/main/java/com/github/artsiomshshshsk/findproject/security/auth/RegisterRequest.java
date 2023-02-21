package com.github.artsiomshshshsk.findproject.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
public record RegisterRequest(

  @NotBlank
  String username,

  @NotBlank
  String email,

  @NotBlank
  String password,

  MultipartFile resume
){}
