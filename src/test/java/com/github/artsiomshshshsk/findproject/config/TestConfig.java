package com.github.artsiomshshshsk.findproject.config;

import com.github.artsiomshshshsk.findproject.security.config.JwtService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Import(JwtService.class)
public class TestConfig {
}
