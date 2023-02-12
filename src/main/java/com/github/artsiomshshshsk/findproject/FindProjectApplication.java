package com.github.artsiomshshshsk.findproject;


import com.github.artsiomshshshsk.findproject.config.S3ConfigProperties;
import com.github.artsiomshshshsk.findproject.security.config.JWTConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JWTConfigProperties.class, S3ConfigProperties.class})
public class FindProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindProjectApplication.class, args);

    }

}
