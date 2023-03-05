package com.github.artsiomshshshsk.findproject.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@ConfigurationProperties("aws.s3")
@Profile("prod")
public record S3ConfigProperties(
        String accessKey,
        String secretKey,
        String region,
        String bucketName,
        String endpoint
) {
}
