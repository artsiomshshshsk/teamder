package com.github.artsiomshshshsk.findproject.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private final AuthenticationProvider authenticationProvider;

  private final JwtAuthenticationFilter jwtAuthFilter;

  private final AuthEntryPointJwt unauthorizedHandler;

  private final CustomAccessDeniedHandler accessDeniedHandler;

  private final CustomAuthenticationEntryPoint authenticationEntryPoint;

  public SecurityConfiguration(AuthenticationProvider authenticationProvider,
                               JwtAuthenticationFilter jwtAuthFilter,
                               AuthEntryPointJwt unauthorizedHandler,
                               CustomAccessDeniedHandler accessDeniedHandler,
                               CustomAuthenticationEntryPoint authenticationEntryPoint) {
    this.authenticationProvider = authenticationProvider;
    this.jwtAuthFilter = jwtAuthFilter;
    this.unauthorizedHandler = unauthorizedHandler;
    this.accessDeniedHandler = accessDeniedHandler;
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Value("${app.baseUrl}")
  private String baseUrl;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint).and()
        .authorizeHttpRequests()
        .antMatchers("/api/auth/**",
                    "/h2-console/**",
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/v2/api-docs",
                    "/webjars/**",
                    "/verify",
                    "/process_register",
                    "/api/mock/**").permitAll()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
        .antMatchers(HttpMethod.GET, "/api/projects/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .headers().frameOptions().sameOrigin()
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }


  @Bean
  public WebMvcConfigurer corsConfigurationSource() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
      }
    };
  }


}