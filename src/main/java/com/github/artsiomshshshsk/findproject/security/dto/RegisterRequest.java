package com.github.artsiomshshshsk.findproject.security.dto;

import lombok.Builder;
import javax.validation.constraints.NotBlank;

@Builder
public record RegisterRequest(

  @NotBlank
  String username,

  @NotBlank
  String email,

  @NotBlank
  String password
){}
