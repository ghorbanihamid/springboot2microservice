package com.soshiant.springbootexample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.PathSelectors.regex;

@Profile("dev")
@Configuration
public class SwaggerConfig {

  @Value("${application.version}")
  private String applicationVersion;

  @Value("${application.name}")
  private String applicationName;

  @Value("${application.description}")
  private String applicationDescription;

  @Bean
  public Docket postsApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("soshiant")
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(regex("/soshiant/sbe/.*"))
        .build()
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title(applicationName)
        .description(applicationDescription)
        .version(applicationVersion)
        .build();
  }
}
