package com.github.artsiomshshshsk.findproject.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record JWTConfigProperties(
        String secret,
        long expirationTime
) {
}
