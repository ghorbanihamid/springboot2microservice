package com.soshiant.springbootexample.config;

import com.soshiant.springbootexample.config.properties.CryptographyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean
  public CryptographyProperties cryptographyProperties() {
    return new CryptographyProperties();
  }

}
