package com.github.artsiomshshshsk.findproject.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class AWSConfig {

    private final S3ConfigProperties s3ConfigProperties;

    public AWSCredentials credentials() {
        return new BasicAWSCredentials(
                s3ConfigProperties.accessKey(),
                s3ConfigProperties.secretKey()
        );
    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder

                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        s3ConfigProperties.endpoint(), s3ConfigProperties.region()
                ))
                .withPathStyleAccessEnabled(true)
                .build();
    }

}