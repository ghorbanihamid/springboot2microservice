package com.soshiant.springbootexample.config;

import com.soshiant.springbootexample.config.properties.CryptographyProperties;
import com.soshiant.springbootexample.util.AppConstants;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ApplicationConfig {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CryptographyProperties cryptographyProperties() {
    return new CryptographyProperties();
  }

  @Bean
  public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.setReadTimeout(Duration.ofMillis(AppConstants.SOCKET_TIMEOUT))
            .setConnectTimeout(Duration.ofMillis(AppConstants.CONNECTION_TIMEOUT)).build();
  }
}
