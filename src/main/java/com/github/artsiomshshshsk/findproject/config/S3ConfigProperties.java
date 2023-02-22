package com.github.artsiomshshshsk.findproject.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aws.s3")
public record S3ConfigProperties(
        String accessKey,
        String secretKey,
        String region,
        String bucketName,
        String endpoint
) {
}
